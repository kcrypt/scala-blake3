package ky.korins.blake3

import CommonFunction._

import java.io.InputStream
import java.nio.{ByteBuffer, CharBuffer}

// An incremental hasher that can accept any number of writes.
private[blake3] class HasherImpl (
  val key: Array[Int],
  val flags: Int
) extends Hasher {

  val chunkState: ChunkState = new ChunkState(key, 0, flags)

  // Space for 54 subtree chaining values
  val cvStack: Array[Array[Int]] = new Array[Array[Int]](MAX_DEPTH)

  var cvStackLen: Int = 0

  private val tmpBlockWords = new Array[Int](BLOCK_LEN_WORDS)

  private def pushStack(cv: Array[Int]): Unit = {
    cvStack(cvStackLen) = cv
    cvStackLen += 1
  }

  private def popStack(): Array[Int] = {
    cvStackLen -= 1
    cvStack(cvStackLen)
  }

  // Section 5.1.2 of the BLAKE3 spec explains this algorithm in more detail.
  private def addChunkChainingValue(cv: Array[Int], chunks: Long): Unit = {
    // This chunk might complete some subtrees. For each completed subtree,
    // its left child will be the current top entry in the CV stack, and
    // its right child will be the current value of `newCv`. Pop each left
    // child off the stack, merge it with `newCv`, and overwrite `newCv`
    // with the result. After all these merges, push the final value of
    // `newCv` onto the stack. The number of completed subtrees is given
    // by the number of trailing 0-bits in the new total number of chunks.
    var totalChunks = chunks
    while ((totalChunks & 1) == 0) {
      parentCV(cv, popStack(), cv, key, flags, tmpBlockWords)
      totalChunks >>= 1
    }
    pushStack(cv)
  }

  private def finalizeWhenCompleted(): Int = {
    val len = chunkState.len()
    // If the current chunk is complete, finalize it and reset the
    // chunk state. More input is coming, so this chunk is not ROOT.
    if (len == CHUNK_LEN) {
      val chunkCV = new Array[Int](BLOCK_LEN_WORDS)
      chunkState.unsafeOutput().chainingValue(chunkCV)
      val totalChunks = chunkState.reset(key)
      addChunkChainingValue(chunkCV, totalChunks)
      0
    } else len
  }

  // Add input to the hash state. This can be called any number of times.
  def update(input: Array[Byte], offset: Int, len: Int): Hasher = synchronized {
    var i = offset
    val end = offset + len
    while (i < end) {
      val len = finalizeWhenCompleted()
      val consume = Math.min(CHUNK_LEN - len, end - i)
      chunkState.update(input, i, i + consume)
      i += consume
    }
    this
  }

  def update(input: Array[Byte]): Hasher =
    update(input, 0, input.length)

  def update(input: String): Hasher =
    update(input.getBytes)

  // Simplified version of update(Array[Byte])
  def update(input: Byte): Hasher = synchronized {
    finalizeWhenCompleted()
    chunkState.update(input)
    this
  }

  def update(input: InputStream): Hasher = synchronized {
    val bytes = new Array[Byte](CHUNK_LEN)

    var consume = chunkState.len() match {
      case 0 | CHUNK_LEN =>
        CHUNK_LEN
      case len =>
        CHUNK_LEN - len
    }

    var read = input.read(bytes, 0, consume)
    while (read > 0) {
      val len = finalizeWhenCompleted()
      chunkState.update(bytes, 0, read)
      consume = CHUNK_LEN - len
      read = input.read(bytes, 0, consume)
    }

    this
  }

  def update(input: ByteBuffer): Hasher = synchronized {
    val bytes = new Array[Byte](CHUNK_LEN)

    while (input.hasRemaining) {
      val len = finalizeWhenCompleted()
      val consume = Math.min(CHUNK_LEN - len, input.remaining())
      input.get(bytes, 0, consume)
      chunkState.update(bytes, 0, consume)
    }

    this
  }

  private def getOutput: Output = synchronized {
    if (cvStackLen == 0) chunkState.output()
    else {
      // Starting with the Output from the current chunk, compute all the
      // parent chaining values along the right edge of the tree, until we
      // have the root Output.
      var output = chunkState.unsafeOutput()
      val cv = new Array[Int](BLOCK_LEN_WORDS)
      val blockWords = new Array[Int](BLOCK_LEN_WORDS)
      var parentNodesRemaining = cvStackLen
      while (parentNodesRemaining > 0) {
        parentNodesRemaining -= 1
        output.chainingValue(cv)
        output = parentOutput(blockWords, cvStack(parentNodesRemaining),
          cv,
          key,
          flags
        )
      }
      output
    }
  }

  // Finalize the hash and write any number of output bytes.
  def done(out: Array[Byte], offset: Int, len: Int): Unit =
    getOutput.rootBytes(out, offset, len)

  def done(out: Array[Byte]): Unit =
    done(out, 0, out.length)

  // Finalize the hash and write one byte.
  def done(): Byte =
    getOutput.rootByte()
}
