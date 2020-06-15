package ky.korins.blake3

import java.nio.{ByteBuffer, ByteOrder}

private[blake3] object CommonFunction {
  // The mixing function, G, which mixes either a column or a diagonal.
  // this function uses mutable of Word
  def g(state: Array[Int], a: Int, b: Int, c: Int, d: Int, mx: Int, my: Int): Unit = {
    state(a) = state(a) + state(b) + mx
    state(d) = Integer.rotateRight(state(d) ^ state(a), 16)
    state(c) = state(c) + state(d)
    state(b) = Integer.rotateRight(state(b) ^ state(c), 12)
    state(a) = state(a) + state(b) + my
    state(d) = Integer.rotateRight(state(d) ^ state(a), 8)
    state(c) = state(c) + state(d)
    state(b) = Integer.rotateRight(state(b) ^ state(c), 7)
  }

  // this function uses mutable of Word
  def round(state: Array[Int], m: Array[Int]): Unit = {
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
  def permute(m: Array[Int]): Array[Int] = {
    val permuted = new Array[Int](16)

    var i = 0
    while (i < m.length) {
      permuted(i) = m(MSG_PERMUTATION(i))
      i += 1
    }

    permuted
  }

  def compress(
    chainingValue: Vector[Int],
    blockWords: Vector[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Vector[Int] = {
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
      counter.toInt,
      (counter >> 32).toInt,
      blockLen,
      flags
    )

    var block = blockWords.toArray

    // rounds 1..7
    var i = 0
    while (i < 7) {
      round(state, block)
      block = permute(block)
      i += 1
    }

    i = 0
    while (i < 8) {
      state(i) ^= state(i + 8)
      state(i + 8) ^= chainingValue(i)
      i += 1
    }

    state.toVector
  }


  def first8Words(compressionOutput: Vector[Int]): Vector[Int] =
    compressionOutput.take(8)

  def wordsFromLittleEndianBytes(bytes: Array[Byte]): Vector[Int] =
    bytes.grouped(4) // bytes per word
      .map { bytes =>
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt()
      }.toVector

  def parentOutput(
    leftChildCV: Vector[Int], rightChildCv: Vector[Int], key: Vector[Int], flags: Int
  ): Output =
    new Output(key, leftChildCV ++ rightChildCv, 0, BLOCK_LEN, flags | PARENT)

  def parentCV(
    leftChildCV: Vector[Int], rightChildCv: Vector[Int], key: Vector[Int], flags: Int
  ): Vector[Int] =
    parentOutput(leftChildCV, rightChildCv, key, flags).chainingValue()

}
