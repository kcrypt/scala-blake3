package ky.korins.blake3

import CommonFunction._

private[blake3] object HasherImpl {
  val emptySubtree: Array[Int] = new Array[Int](KEY_LEN_WORDS)
}

// An incremental hasher that can accept any number of writes.
private[blake3] class HasherImpl (
  var chunkState: ChunkState,
  val key: Array[Int],
  val cvStack: Array[Array[Int]], // Space for 54 subtree chaining values:
  var cvStackLen: Int, // 2^54 * CHUNK_LEN = 2^64
  val flags: Int
) extends Hasher {
  def this(key: Array[Int], flags: Int) =
    this(new ChunkState(key, 0, flags), key,
      Array.fill[Array[Int]](MAX_DEPTH)(HasherImpl.emptySubtree), 0, flags)

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
    var newCv = cv
    var totalChunks = chunks
    while ((totalChunks & 1) == 0) {
      newCv = parentCV(popStack(), newCv, key, flags)
      totalChunks >>= 1
    }
    pushStack(newCv)
  }

  private def finalizeWhenCompleted(): Int = {
    val len = chunkState.len()
    // If the current chunk is complete, finalize it and reset the
    // chunk state. More input is coming, so this chunk is not ROOT.
    if (len == CHUNK_LEN) {
      val chunkCV = chunkState.output().chainingValue()
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

  private def getOutput: Output = synchronized {
    // Starting with the Output from the current chunk, compute all the
    // parent chaining values along the right edge of the tree, until we
    // have the root Output.
    var output = chunkState.output()
    var parentNodesRemaining = cvStackLen
    while (parentNodesRemaining > 0) {
      parentNodesRemaining -= 1
      output = parentOutput(cvStack(parentNodesRemaining),
        output.chainingValue(),
        key,
        flags
      )
    }
    output
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
