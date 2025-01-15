/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * Supported since 2022 by Kcrypt Lab UG <opensource@kcry.pt>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

package pt.kcry.blake3

import CompressRounds._

import java.io.{InputStream, OutputStream}
import java.nio.ByteBuffer

// An incremental hasher that can accept any number of writes.
private[blake3] class HasherImpl(val key: Array[Int], val flags: Int)
    extends Hasher {

  private val tmpChunkCV = new Array[Int](BLOCK_LEN_WORDS)
  private val tmpBlockWords = new Array[Int](BLOCK_LEN_WORDS)

  private val chunkState =
    new ChunkState(key, 0, flags, tmpChunkCV, tmpBlockWords)

  private val output =
    new Output(key, tmpBlockWords, BLOCK_LEN, flags, tmpChunkCV)

  // Space for 54 subtree chaining values
  private val cvStack: Array[Array[Int]] = {
    val cvStack = new Array[Array[Int]](MAX_DEPTH)
    var i = 0
    while (i < MAX_DEPTH) {
      cvStack(i) = new Array[Int](BLOCK_LEN_WORDS)
      i += 1
    }
    cvStack
  }

  private var cvStackLen: Int = 0

  // Section 5.1.2 of the BLAKE3 spec explains this algorithm in more detail.
  private def finalizeWhenCompleted(): Int = {
    val len = chunkState.len()
    // If the current chunk is complete, finalize it and reset the
    // chunk state. More input is coming, so this chunk is not ROOT.
    if (len == CHUNK_LEN) {
      chunkState.roundBlock()
      chunkState.chainingValue(tmpChunkCV)
      var totalChunks = chunkState.reset(key)

      // This chunk might complete some subtrees. For each completed subtree,
      // its left child will be the current top entry in the CV stack, and
      // its right child will be the current value of `newCv`. Pop each left
      // child off the stack, merge it with `newCv`, and overwrite `newCv`
      // with the result. After all these merges, push the final value of
      // `newCv` onto the stack. The number of completed subtrees is given
      // by the number of trailing 0-bits in the new total number of chunks.
      while ((totalChunks & 1) == 0) {
        cvStackLen -= 1
        mergeChildCV(tmpBlockWords, cvStack(cvStackLen), tmpChunkCV)
        compressRounds(
          tmpChunkCV, tmpBlockWords, key, 0, BLOCK_LEN, flags | PARENT
        )
        totalChunks >>= 1
      }

      System.arraycopy(tmpChunkCV, 0, cvStack(cvStackLen), 0, BLOCK_LEN_WORDS)
      cvStackLen += 1
      0
    } else len
  }

  // Add input to the hash state. This can be called any number of times.
  override def update(input: Array[Byte], offset: Int, len: Int): Hasher =
    synchronized {
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

  // Simplified version of update(Array[Byte])
  override def update(input: Byte): Hasher = synchronized {
    finalizeWhenCompleted()
    chunkState.update(input)
    this
  }

  // Simplified version of update(Array[Byte])
  override def update(input: Short): Hasher = synchronized {
    var v = input
    var i = 0
    while (i < 2) {
      finalizeWhenCompleted()
      chunkState.update(v.toByte)
      v = (v >> 8).toShort
      i += 1
    }
    this
  }

  // Simplified version of update(Array[Byte])
  override def update(input: Int): Hasher = synchronized {
    var v = input
    var i = 0
    while (i < 4) {
      finalizeWhenCompleted()
      chunkState.update(v.toByte)
      v >>= 8
      i += 1
    }
    this
  }

  // Simplified version of update(Array[Byte])
  override def update(input: Long): Hasher = synchronized {
    var v = input
    var i = 0
    while (i < 8) {
      finalizeWhenCompleted()
      chunkState.update(v.toByte)
      v >>= 8
      i += 1
    }
    this
  }

  override def update(input: InputStream, len: Int): Hasher = synchronized {
    val bytes = new Array[Byte](CHUNK_LEN)

    var consume = chunkState.len() match {
      case 0 | CHUNK_LEN => CHUNK_LEN
      case len           => CHUNK_LEN - len
    }

    var read = input.read(bytes, 0, consume)
    var remaining = len - read
    while (remaining > 0 && read >= 0) {
      val len = finalizeWhenCompleted()
      chunkState.update(bytes, 0, read)
      consume = CHUNK_LEN - len
      read = input.read(bytes, 0, consume)
      remaining -= read
    }

    this
  }

  override def update(input: ByteBuffer, len: Int): Hasher = synchronized {
    val bytes = new Array[Byte](CHUNK_LEN)

    var remaining = len
    while (remaining > 0 && input.hasRemaining) {
      val chunkLen = finalizeWhenCompleted()
      val consume = Math.min(CHUNK_LEN - chunkLen, remaining)
      input.get(bytes, 0, consume)
      chunkState.update(bytes, 0, consume)
      remaining -= consume
    }

    this
  }

  private def getOutput: Output = {
    // let start from round block
    chunkState.roundBlock()

    // Starting with the Output from the current chunk, compute all the
    // parent chaining values along the right edge of the tree, until we
    // have the root Output.
    var parentNodesRemaining = cvStackLen
    var inputChainingValue = chunkState.chainingValue
    var counter = chunkState.chunkCounter
    var blockLen = chunkState.blockLen
    var outputFlags = flags | chunkState.startFlag() | CHUNK_END

    while (parentNodesRemaining > 0) {
      parentNodesRemaining -= 1

      compressRounds(
        tmpChunkCV, tmpBlockWords, inputChainingValue, counter, blockLen,
        outputFlags
      )

      // emulate reset
      inputChainingValue = key
      counter = 0
      blockLen = BLOCK_LEN
      outputFlags = flags | PARENT

      mergeChildCV(tmpBlockWords, cvStack(parentNodesRemaining), tmpChunkCV)
    }

    // reset cached output
    output.inputChainingValue = inputChainingValue
    output.blockLen = blockLen
    output.flags = outputFlags

    output
  }

  @inline
  private def mergeChildCV(
      merged: Array[Int], leftChildCV: Array[Int], rightChildCv: Array[Int]
  ): Unit = {
    System.arraycopy(rightChildCv, 0, merged, KEY_LEN_WORDS, KEY_LEN_WORDS)
    System.arraycopy(leftChildCV, 0, merged, 0, KEY_LEN_WORDS)
  }

  // Finalize the hash and write any number of output bytes.
  override def done(out: Array[Byte], offset: Int, len: Int): Unit =
    synchronized(getOutput.rootBytes(out, offset, len))

  // Finalize the hash and write one byte.
  override def done(): Byte = synchronized(getOutput.rootByte())

  override def doneShort(): Short = synchronized(getOutput.rootShort())

  override def doneInt(): Int = synchronized(getOutput.rootInt())

  override def doneLong(): Long = synchronized(getOutput.rootLong())

  override def doneCallBack[T](out: Byte => T, len: Int): Unit =
    synchronized(getOutput.rootBytes(out, len))

  // avoid callback here to prevent make a call GC friendly
  override def done(out: OutputStream, len: Int): Unit =
    synchronized(getOutput.rootBytes(out, len))

  // avoid callback here to prevent make a call GC friendly
  override def done(out: ByteBuffer, len: Int): Unit =
    synchronized(getOutput.rootBytes(out, len))

  override def doneXor(
      in: Array[Byte], inOff: Int, out: Array[Byte], outOff: Int, len: Int
  ): Unit = synchronized(getOutput.rootBytesXor(in, inOff, out, outOff, len))

  override def doneXor(in: InputStream, out: OutputStream, len: Int): Unit =
    synchronized(getOutput.rootBytesXor(in, out, len))

  override def doneXor(in: ByteBuffer, out: ByteBuffer, len: Int): Unit =
    synchronized(getOutput.rootBytesXor(in, out, len))

  override def doneXorCallBack[T](
      in: () => Byte, out: Byte => T, len: Int
  ): Unit = synchronized(getOutput.rootBytesXor(in, out, len))
}
