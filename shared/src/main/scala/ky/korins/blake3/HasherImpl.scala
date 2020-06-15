package ky.korins.blake3

import CommonFunction._

// An incremental hasher that can accept any number of writes.
private[blake3] class HasherImpl (
  var chunkState: ChunkState,
  val key: Vector[Int],
  val cvStack: Array[Vector[Int]], // Space for 54 subtree chaining values:
  var cvStackLen: Int, // 2^54 * CHUNK_LEN = 2^64
  val flags: Int
) extends Hasher {
  def this(key: Vector[Int], flags: Int) =
    this(new ChunkState(key, 0, flags), key,
      Array.fill[Vector[Int]](54)(Array.fill[Int](8)(0).toVector), 0, flags)

  private def pushStack(cv: Vector[Int]): Unit = {
    cvStack(cvStackLen) = cv
    cvStackLen += 1
  }

  private def popStack(): Vector[Int] = {
    cvStackLen -= 1
    cvStack(cvStackLen)
  }


  // Section 5.1.2 of the BLAKE3 spec explains this algorithm in more detail.
  private def add_chunk_chaining_value(cv: Vector[Int], chunks: Long): Vector[Int] = {
    // This chunk might complete some subtrees. For each completed subtree,
    // its left child will be the current top entry in the CV stack, and
    // its right child will be the current value of `new_cv`. Pop each left
    // child off the stack, merge it with `new_cv`, and overwrite `new_cv`
    // with the result. After all these merges, push the final value of
    // `new_cv` onto the stack. The number of completed subtrees is given
    // by the number of trailing 0-bits in the new total number of chunks.
    var new_cv = cv
    var total_chunks = chunks
    while ((total_chunks & 1) == 0) {
      new_cv = parentCV(popStack(), new_cv, key, flags)
      total_chunks >>= 1
    }
    pushStack(new_cv)
    new_cv
  }

  // Add input to the hash state. This can be called any number of times.
  def update(input: Array[Byte]): Hasher = {
    var i = 0
    val end = input.length
    while (i < input.length) {
      val len = chunkState.len()
      // If the current chunk is complete, finalize it and reset the
      // chunk state. More input is coming, so this chunk is not ROOT.
      if (len == CHUNK_LEN) {
        val chunkCV = chunkState.output().chainingValue()
        val totalChunks = chunkState.chunkCounter + 1
        add_chunk_chaining_value(chunkCV, totalChunks)
        chunkState = new ChunkState(key, totalChunks, flags)
      }
      val consume = Math.min(CHUNK_LEN - len, end - i)
      chunkState.update(input, i, i + consume)
      i += consume
    }
    this
  }

  // Finalize the hash and write any number of output bytes.
  def done(out: Array[Byte]): Unit = {
    // Starting with the Output from the current chunk, compute all the
    // parent chaining values along the right edge of the tree, until we
    // have the root Output.
    var output = chunkState.output()
    var parentNodesRemaining = cvStackLen.toInt
    while (parentNodesRemaining > 0) {
      parentNodesRemaining -= 1
      output = parentOutput(cvStack(parentNodesRemaining),
        output.chainingValue(),
        key,
        flags
      )
    }
    output.root_output_bytes(out)
  }
}
