package io.electt.shared.crypto

import scodec.bits._
import spire.math._
import spire.syntax.all._

object Blake3 {

  // constants
  val OUT_LEN: Int = 32
  val KEY_LEN: Int = 32
  val BLOCK_LEN: Int = 64
  val CHUNK_LEN: Int = 1024

  private val CHUNK_START = ui"1" << 0
  private val CHUNK_END = ui"1" << 1
  private val PARENT = ui"1" << 2
  private val ROOT = ui"1" << 3
  private val KEYED_HASH = ui"1" << 4
  private val DERIVE_KEY_CONTEXT = ui"1" << 5
  private val DERIVE_KEY_MATERIAL = ui"1" << 6

  private val IV = Vector(
    UInt(0x6A09E667),
    UInt(0xBB67AE85),
    UInt(0x3C6EF372),
    UInt(0xA54FF53A),
    UInt(0x510E527F),
    UInt(0x9B05688C),
    UInt(0x1F83D9AB),
    UInt(0x5BE0CD19)
  )

  private val MSG_PERMUTATION = Vector(
    2, 6, 3, 10, 7, 0, 4, 13, 1, 11, 12, 5, 9, 14, 15, 8
  )

  // The mixing function, G, which mixes either a column or a diagonal.
  // this function uses mutable of Word
  private def g(state: Array[UInt], a: Int, b: Int, c: Int, d: Int, mx: UInt, my: UInt): Unit = {
    state(a) = state(a) + state(b) + mx
    state(d) = (state(d) ^ state(a)) rotateRight 16
    state(c) = state(c) + state(d)
    state(b) = (state(b) ^ state(c)) rotateRight 12
    state(a) = state(a) + state(b) + my
    state(d) = (state(d) ^ state(a)) rotateRight 8
    state(c) = state(c) + state(d)
    state(b) = (state(b) ^ state(c)) rotateRight 7
  }

  // this function uses mutable of Word
  private def round(state: Array[UInt], m: Array[UInt]): Unit = {
    // Mix the columns.
    g(state, 0, 4, 8, 12, m(0), m(1))
    g(state, 1, 5, 9, 13, m(2), m(3))
    g(state, 2, 6, 10, 14, m(4), m(5))
    g(state, 3, 7, 11, 15, m(6), m(7))
    // Mix the diagonals.
    g(state, 0, 5, 10, 15, m(8), m(9))
    g(state, 1, 6, 11, 12, m(10), m(11))
    g(state, 2, 7, 8, 13, m(12), m(13))
    g(state, 3, 4, 9, 14, m(14), m(15))
  }

  // this function uses mutable of Word
  private def permute(m: Array[UInt]): Unit = {
    val permuted = new Array[UInt](16)

    for (i <- m.indices) {
      permuted(i) = m(MSG_PERMUTATION(i))
    }

    for (i <- m.indices) {
      m(i) = permuted(i)
    }
  }

  private def compress(
    chainingValue: Vector[UInt],
    blockWords: Vector[UInt],
    counter: ULong,
    blockLen: UInt,
    flags: UInt
  ): Vector[UInt] = {
    val state = Array(
      chainingValue(0),
      chainingValue(1),
      chainingValue(2),
      chainingValue(3),
      chainingValue(4),
      chainingValue(5),
      chainingValue(6),
      chainingValue(7),
      IV(0),
      IV(1),
      IV(2),
      IV(3),
      UInt(counter.toInt),
      UInt((counter >> 32).toInt),
      blockLen,
      flags
    )

    val block = blockWords.toArray

    // rounds 1..7
    for (_ <- 1 to 7) {
      round(state, block)
      permute(block)
    }

    for (i <- 0 until 8) {
      state(i) ^= state(i + 8);
      state(i + 8) ^= chainingValue(i);
    }

    state.toVector
  }


  private def first8Words(compressionOutput: Vector[UInt]): Vector[UInt] =
    compressionOutput.take(8)

  private def wordsFromLittleEndianBytes(bytes: Array[Byte]): Vector[UInt] =
    bytes.grouped(4) // bytes per word
      .map { bytes =>
        UInt(ByteVector(bytes).toInt(signed = true, ordering = ByteOrdering.LittleEndian))
      }.toVector

  private class Output (
    val inputChainingValue: Vector[UInt],
    val blockWords: Vector[UInt],
    val counter: ULong,
    val blockLen: UInt,
    val flags: UInt
  ) {

    def chainingValue(): Vector[UInt] =
      first8Words(compress(
        inputChainingValue,
        blockWords,
        counter,
        blockLen,
        flags
      ))

    def root_output_bytes(out: Array[Byte]): Unit = {
      var outputBlockCounter = ULong(0)
      for (idxes <- out.indices.grouped(2 * OUT_LEN)) {
        val words = compress(
          inputChainingValue,
          blockWords,
          outputBlockCounter,
          blockLen,
          flags | ROOT
        )
        // The output length might not be a multiple of 4.
        for ((word, idxes) <- words.zip(idxes.grouped(4).toList)) {
          val bytes = ByteVector.fromInt(word.toInt, ordering = ByteOrdering.LittleEndian).toArray
          idxes.zip(bytes).foreach { case (i, b) =>
            out(i) = b
          }
        }
        outputBlockCounter += ULong(1)
      }
    }
  }

  private class ChunkState(
    var chainingValue: Vector[UInt],
    val chunkCounter: ULong,
    val block: Array[Byte],
    var blockLen: Int,
    var blocksCompressed: Int,
    val flags: UInt
  ) {
    def this(key: Vector[UInt], chunkCounter: ULong, flags: UInt) =
      this(key, chunkCounter, Array.fill[Byte](BLOCK_LEN)(0), 0, 0, flags)

    def len(): Int =
      BLOCK_LEN * blocksCompressed.toInt + blockLen.toInt

    private def startFlag(): UInt =
      if (blocksCompressed == 0)
        CHUNK_START
      else ui"0"

    def update(b: Byte): Unit = {
      if (blockLen.toInt == BLOCK_LEN) {
        chainingValue = first8Words(compress(
          chainingValue,
          wordsFromLittleEndianBytes(block),
          chunkCounter,
          UInt(BLOCK_LEN),
          flags | startFlag()
        ))
        blocksCompressed += 1
        for (i <- block.indices) {
          block(i) = 0
        }
        blockLen = 0
      }
      block(blockLen.toInt) = b
      blockLen += 1
    }

    def output(): Output =
      new Output(chainingValue, wordsFromLittleEndianBytes(block),
        chunkCounter, UInt(blockLen.toInt), flags | startFlag() | CHUNK_END)
  }

  private def parentOutput(
    leftChildCV: Vector[UInt], rightChildCv: Vector[UInt], key: Vector[UInt], flags: UInt,
  ): Output =
    new Output(key, leftChildCV ++ rightChildCv, ul"0", UInt(BLOCK_LEN), flags | PARENT)

  private def parentCV(
    leftChildCV: Vector[UInt], rightChildCv: Vector[UInt], key: Vector[UInt], flags: UInt
  ): Vector[UInt] =
    parentOutput(leftChildCV, rightChildCv, key, flags).chainingValue()

  // An incremental hasher that can accept any number of writes.
  private class HasherImpl (
    var chunkState: ChunkState,
    val key: Vector[UInt],
    val cvStack: Array[Vector[UInt]], // Space for 54 subtree chaining values:
    var cvStackLen: Int, // 2^54 * CHUNK_LEN = 2^64
    val flags: UInt
    ) extends Hasher {

    def this(key: Vector[UInt], flags: UInt) =
      this(new ChunkState(key, ul"0", flags), key,
        Array.fill[Vector[UInt]](54)(Array.fill[UInt](8)(ui"0").toVector), 0, flags)

    private def pushStack(cv: Vector[UInt]): Unit = {
      cvStack(cvStackLen) = cv
      cvStackLen += 1
    }

    private def popStack(): Vector[UInt] = {
      cvStackLen -= 1
      cvStack(cvStackLen)
    }


    // Section 5.1.2 of the BLAKE3 spec explains this algorithm in more detail.
    private def add_chunk_chaining_value(cv: Vector[UInt], chunks: ULong): Vector[UInt] = {
      // This chunk might complete some subtrees. For each completed subtree,
      // its left child will be the current top entry in the CV stack, and
      // its right child will be the current value of `new_cv`. Pop each left
      // child off the stack, merge it with `new_cv`, and overwrite `new_cv`
      // with the result. After all these merges, push the final value of
      // `new_cv` onto the stack. The number of completed subtrees is given
      // by the number of trailing 0-bits in the new total number of chunks.
      var new_cv = cv
      var total_chunks = chunks
      while ((total_chunks & ul"1") == ul"0") {
        new_cv = parentCV(popStack(), new_cv, key, flags)
        total_chunks >>= 1
      }
      pushStack(new_cv)
      new_cv
    }

    // Add input to the hash state. This can be called any number of times.
    def update(input: Array[Byte]): Hasher = {
      for (b <- input) {
        // If the current chunk is complete, finalize it and reset the
        // chunk state. More input is coming, so this chunk is not ROOT.
        if (chunkState.len() == CHUNK_LEN) {
          val chunkCV = chunkState.output().chainingValue()
          val totalChunks = chunkState.chunkCounter + ul"1";
          add_chunk_chaining_value(chunkCV, totalChunks)
          chunkState = new ChunkState(key, totalChunks, flags)
        }
        chunkState.update(b)
      }
      this
    }

    // Finalize the hash and write any number of output bytes.
    def finalize(out: Array[Byte]): Unit = {
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

  trait Hasher {
    def update(input: Array[Byte]): Hasher

    def finalize(out_slice: Array[Byte]): Unit

    def finalize(len: Int): Array[Byte] = {
      val bytes = Array.fill[Byte](len)(0)
      finalize(bytes)
      bytes
    }

    // hex representative of finalized bytes
    def finalizeHex(len: Int): String =
      finalize(len).map(b => "%02x" format (b & 0xff)).mkString
  }

  def newHasher(): Hasher =
    new HasherImpl(IV, ui"0")

  def newKeyedHasher(key: Array[Byte]): Hasher = {
    assert(key.length == KEY_LEN, "key should be Blake3.KEY_LEN bytes")
    new HasherImpl(wordsFromLittleEndianBytes(key), KEYED_HASH)
  }

  def newDeriveKeyHasher(context: String): Hasher = {
    val contextKey = new HasherImpl(IV, DERIVE_KEY_CONTEXT)
      .update(context.getBytes)
      .finalize(KEY_LEN)
    val contextKeyWords = first8Words(wordsFromLittleEndianBytes(contextKey))
    new HasherImpl(contextKeyWords, DERIVE_KEY_MATERIAL)
  }

  def hash(source: Array[Byte], len: Int): Array[Byte] =
    newHasher().update(source).finalize(len)

  def hex(source: Array[Byte], resultLength: Int): String = {
    assert(resultLength % 2 == 0, "resultLength should be even")
    newHasher().update(source).finalizeHex(resultLength / 2)
  }

  def bigInt(source: Array[Byte], bitLength: Int): BigInt = {
    assert(bitLength % 8 == 0, "bitLength should be a multiple of 8")
    BigInt(newHasher().update(source).finalize(bitLength / 8))
  }

}
