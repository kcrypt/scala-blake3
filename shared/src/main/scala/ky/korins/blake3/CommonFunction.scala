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
  @inline
  def permute(m: Array[Int], permuted: Array[Int]): Unit = {
    permuted(0) = m(2)
    permuted(1) = m(6)
    permuted(2) = m(3)
    permuted(3) = m(10)
    permuted(4) = m(7)
    permuted(5) = m(0)
    permuted(6) = m(4)
    permuted(7) = m(13)
    permuted(8) = m(1)
    permuted(9) = m(11)
    permuted(10) = m(12)
    permuted(11) = m(5)
    permuted(12) = m(9)
    permuted(13) = m(14)
    permuted(14) = m(15)
    permuted(15) = m(8)
  }

  def initCompressState(
    state: Array[Int],
    chainingValue: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Unit = {
    // CV 0..7
    System.arraycopy(chainingValue, 0, state, 0, 8)
    // IV 0..3
    System.arraycopy(IV, 0, state, 8, 4)

    state(12) = counter.toInt
    state(13) = (counter >> 32).toInt
    state(14) = blockLen
    state(15) = flags
  }

  private def compressRounds(
    state: Array[Int],
    blockWords: Array[Int],
    window1: Array[Int],
    window2: Array[Int]
  ): Unit = {
    System.arraycopy(blockWords, 0, window1, 0, BLOCK_LEN_WORDS)

    // round 1
    round(state, window1)
    permute(window1, window2)

    // round 2
    round(state, window2)
    permute(window2, window1)

    // round 3
    round(state, window1)
    permute(window1, window2)

    // round 4
    round(state, window2)
    permute(window2, window1)

    // round 5
    round(state, window1)
    permute(window1, window2)

    // round 6
    round(state, window2)
    permute(window2, window1)

    // round 7
    round(state, window1)
  }

  @inline
  def newCompressState(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Array[Int] = {
    val state = new Array[Int](BLOCK_LEN_WORDS)
    val window1 = new Array[Int](BLOCK_LEN_WORDS)
    val window2 = new Array[Int](BLOCK_LEN_WORDS)

    initCompressState(state, chainingValue, counter, blockLen, flags)
    compressRounds(state, blockWords, window1, window2)

    state
  }

  @inline
  private def doneCompression(
    state: Array[Int],
    chainingValue: Array[Int]
  ): Unit = {
    var i = 0
    while (i < 8) {
      state(i) ^= state(i + 8)
      state(i + 8) ^= chainingValue(i)
      i += 1
    }
  }

  def compress(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Array[Int] = {
    val state = newCompressState(chainingValue, blockWords, counter, blockLen, flags)

    doneCompression(state, chainingValue)

    state
  }

  def compress(
    state: Array[Int],
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int,
    window1: Array[Int],
    window2: Array[Int]
  ): Array[Int] = {

    initCompressState(state, chainingValue, counter, blockLen, flags)
    compressRounds(state, blockWords, window1, window2)
    doneCompression(state, chainingValue)

    state
  }

  def compressSingle(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Int = {
    val state = newCompressState(chainingValue, blockWords, counter, blockLen, flags)

    // a fast-track for single byte
    state(0) ^ state(8)
  }

  @inline
  def littleEndian2Int(bytes: Array[Byte], off: Int): Int = {
    ((bytes(3 + off) & 0xff) << 24) +
      ((bytes(2 + off) & 0xff) << 16) +
      ((bytes(1 + off) & 0xff) << 8) +
      ((bytes(0 + off) & 0xff) << 0)
  }

  def wordsFromLittleEndianBytes(bytes: Array[Byte], offset: Int, res: Array[Int]): Unit = {
    var i = 0
    var off = offset
    while (i < res.length) {
      res(i) = littleEndian2Int(bytes, off)
      i += 1
      off += 4
    }
  }

  def wordsFromLittleEndianBytes(bytes: Array[Byte]): Array[Int] = {
    val res = new Array[Int](bytes.length / 4)
    wordsFromLittleEndianBytes(bytes, 0, res)
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
