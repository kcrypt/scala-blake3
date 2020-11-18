package ky.korins.blake3

private[blake3] object CommonFunction {
  @inline
  private def rotateRight(i: Int, distance: Int): Int =
    (i >>> distance) | (i << -distance)

  // The mixing function, G, which mixes either a column or a diagonal.
  // this function uses mutable of Word
  def g(state: Array[Int], a: Int, b: Int, c: Int, d: Int, mx: Int, my: Int): Unit = {
    state(a) = state(a) + state(b) + mx
    state(d) = rotateRight(state(d) ^ state(a), 16)
    state(c) = state(c) + state(d)
    state(b) = rotateRight(state(b) ^ state(c), 12)
    state(a) = state(a) + state(b) + my
    state(d) = rotateRight(state(d) ^ state(a), 8)
    state(c) = state(c) + state(d)
    state(b) = rotateRight(state(b) ^ state(c), 7)
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
  def permute(m: Array[Int], permuted: Array[Int]): Unit = {
    var i = 0
    while (i < BLOCK_LEN_WORDS) {
      permuted(i) = m(MSG_PERMUTATION(i))
      i += 1
    }
  }

  private def compressRounds(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Array[Int] = {
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

    var block = new Array[Int](BLOCK_LEN_WORDS)
    var permuted = new Array[Int](BLOCK_LEN_WORDS)
    var i = 0
    while (i < BLOCK_LEN_WORDS) {
      block(i) = blockWords(i)
      i += 1
    }

    // rounds 1..7
    i = 0
    while (i < 7) {
      round(state, block)
      if (i < 6) {
        permute(block, permuted)
        val newBlock = permuted
        permuted = block
        block = newBlock
      }
      i += 1
    }

    state
  }

  def compress(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Array[Int] = {
    val state = compressRounds(chainingValue, blockWords, counter, blockLen, flags)

    var i = 0
    while (i < 8) {
      state(i) ^= state(i + 8)
      state(i + 8) ^= chainingValue(i)
      i += 1
    }

    state
  }

  def compressSingle(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Int = {
    val state = compressRounds(chainingValue, blockWords, counter, blockLen, flags)

    state(0) ^ state(8)
  }

  @inline
  def littleEndian2Int(bytes: Array[Byte], off: Int): Int = {
    ((bytes(3 + off) & 0xff) << 24) +
      ((bytes(2 + off) & 0xff) << 16) +
      ((bytes(1 + off) & 0xff) << 8) +
      ((bytes(0 + off) & 0xff) << 0)
  }

  def wordsFromLittleEndianBytes(bytes: Array[Byte]): Array[Int] = {
    val res = new Array[Int](bytes.length / 4)
    var i = 0
    var off = 0
    while (i < res.length) {
      res(i) = littleEndian2Int(bytes, off)
      i += 1
      off += 4
    }

    res
  }

  def mergeChildCV(leftChildCV: Array[Int], rightChildCv: Array[Int]): Array[Int] = {
    val merged = new Array[Int](16)
    System.arraycopy(leftChildCV, 0, merged, 0, 8)
    System.arraycopy(rightChildCv, 0, merged, 8, 8)
    merged
  }

  def parentOutput(
    leftChildCV: Array[Int], rightChildCv: Array[Int], key: Array[Int], flags: Int
  ): Output =
    new Output(key, mergeChildCV(leftChildCV, rightChildCv), 0, BLOCK_LEN, flags | PARENT)

  def parentCV(
    leftChildCV: Array[Int], rightChildCv: Array[Int], key: Array[Int], flags: Int
  ): Array[Int] =
    parentOutput(leftChildCV, rightChildCv, key, flags).chainingValue()

}
