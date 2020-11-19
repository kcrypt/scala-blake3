package ky.korins.blake3

private[blake3] object CommonFunction {
  // The mixing function, G, which mixes either a column or a diagonal.
  // this function uses mutable of Word
  @inline
  private def g(state: Array[Int], a: Int, b: Int, c: Int, d: Int, mx: Int, my: Int): Unit = {
    var state_a = state(a)
    var state_b = state(b)
    var state_c = state(c)
    var state_d = state(d)

    state_a = state_a + state_b + mx
    state_d = Integer.rotateRight(state_d ^ state_a, 16)
    state_c = state_c + state_d
    state_b = Integer.rotateRight(state_b ^ state_c, 12)
    state_a = state_a + state_b + my
    state_d = Integer.rotateRight(state_d ^ state_a, 8)
    state_c = state_c + state_d
    state_b = Integer.rotateRight(state_b ^ state_c, 7)

    state(a) = state_a
    state(b) = state_b
    state(c) = state_c
    state(d) = state_d
  }

  // this function uses mutable of Word
  @inline
  private def round(state: Array[Int], m: Array[Int], round: Int): Unit = {
    val schedule = MSG_SCHEDULE(round)
    // Mix the columns.
    g(state, 0, 4, 8, 12, m(schedule(0)), m(schedule(1)))
    g(state, 1, 5, 9, 13, m(schedule(2)), m(schedule(3)))
    g(state, 2, 6, 10, 14, m(schedule(4)), m(schedule(5)))
    g(state, 3, 7, 11, 15, m(schedule(6)), m(schedule(7)))
    // Mix the diagonals.
    g(state, 0, 5, 10, 15, m(schedule(8)), m(schedule(9)))
    g(state, 1, 6, 11, 12, m(schedule(10)), m(schedule(11)))
    g(state, 2, 7, 8, 13, m(schedule(12)), m(schedule(13)))
    g(state, 3, 4, 9, 14, m(schedule(14)), m(schedule(15)))
  }

  @inline
  private def compressRounds(
    state: Array[Int],
    blockWords: Array[Int],
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

    round(state, blockWords, 0)
    round(state, blockWords, 1)
    round(state, blockWords, 2)
    round(state, blockWords, 3)
    round(state, blockWords, 4)
    round(state, blockWords, 5)
    round(state, blockWords, 6)
  }

  def compressInPlace(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int,
    tmpState: Array[Int]
  ): Unit = {
    compressRounds(tmpState, blockWords, chainingValue, counter, blockLen, flags)

    chainingValue(0) = tmpState(0) ^ tmpState(8)
    chainingValue(1) = tmpState(1) ^ tmpState(9)
    chainingValue(2) = tmpState(2) ^ tmpState(10)
    chainingValue(3) = tmpState(3) ^ tmpState(11)
    chainingValue(4) = tmpState(4) ^ tmpState(12)
    chainingValue(5) = tmpState(5) ^ tmpState(13)
    chainingValue(6) = tmpState(6) ^ tmpState(14)
    chainingValue(7) = tmpState(7) ^ tmpState(15)

  }

  def compress(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Array[Int] = {
    val state = new Array[Int](BLOCK_LEN_WORDS)

    compressRounds(state, blockWords, chainingValue, counter, blockLen, flags)

    state(0) ^= state(8)
    state(1) ^= state(9)
    state(2) ^= state(10)
    state(3) ^= state(11)
    state(4) ^= state(12)
    state(5) ^= state(13)
    state(6) ^= state(14)
    state(7) ^= state(15)

    state(8) ^= chainingValue(0)
    state(9) ^= chainingValue(1)
    state(10) ^= chainingValue(2)
    state(11) ^= chainingValue(3)
    state(12) ^= chainingValue(4)
    state(13) ^= chainingValue(5)
    state(14) ^= chainingValue(6)
    state(15) ^= chainingValue(7)

    state
  }

  def compressSingle(
    chainingValue: Array[Int],
    blockWords: Array[Int],
    counter: Long,
    blockLen: Int,
    flags: Int
  ): Int = {
    val state = new Array[Int](BLOCK_LEN_WORDS)

    compressRounds(state, blockWords, chainingValue, counter, blockLen, flags)

    // a fast-track for single byte
    state(0) ^ state(8)
  }

  @inline
  def littleEndian2Int(bytes: Array[Byte], off: Int): Int =
    ((bytes(3 + off) & 0xff) << 24) |
      ((bytes(2 + off) & 0xff) << 16) |
      ((bytes(1 + off) & 0xff) << 8) |
      ((bytes(0 + off) & 0xff) << 0)

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
