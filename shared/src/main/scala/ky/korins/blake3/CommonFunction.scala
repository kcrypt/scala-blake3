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
    var `state_d ^ state_a` = state_d ^ state_a
    state_d = (`state_d ^ state_a` >>> 16) | (`state_d ^ state_a` << 16)

    state_c = state_c + state_d
    var `state_b ^ state_c` = state_b ^ state_c
    state_b = (`state_b ^ state_c` >>> 12) | (`state_b ^ state_c` << 20)

    state_a = state_a + state_b + my
    `state_d ^ state_a` = state_d ^ state_a
    state_d = (`state_d ^ state_a` >>> 8) | (`state_d ^ state_a` << 24)

    state_c = state_c + state_d
    `state_b ^ state_c` = state_b ^ state_c
    state_b = (`state_b ^ state_c` >>> 7) | (`state_b ^ state_c` << 25)

    state(a) = state_a
    state(b) = state_b
    state(c) = state_c
    state(d) = state_d
  }

  @inline
  private def round_1(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(0), m(1))
    g(state, 1, 5, 9, 13, m(2), m(3))
    g(state, 2, 6, 10, 14, m(4), m(5))
    g(state, 3, 7, 11, 15, m(6), m(7))

    g(state, 0, 5, 10, 15, m(8), m(9))
    g(state, 1, 6, 11, 12, m(10), m(11))
    g(state, 2, 7, 8, 13, m(12), m(13))
    g(state, 3, 4, 9, 14, m(14), m(15))
  }

  @inline
  private def round_2(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(2), m(6))
    g(state, 1, 5, 9, 13, m(3), m(10))
    g(state, 2, 6, 10, 14, m(7), m(0))
    g(state, 3, 7, 11, 15, m(4), m(13))

    g(state, 0, 5, 10, 15, m(1), m(11))
    g(state, 1, 6, 11, 12, m(12), m(5))
    g(state, 2, 7, 8, 13, m(9), m(14))
    g(state, 3, 4, 9, 14, m(15), m(8))
  }

  @inline
  private def round_3(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(3), m(4))
    g(state, 1, 5, 9, 13, m(10), m(12))
    g(state, 2, 6, 10, 14, m(13), m(2))
    g(state, 3, 7, 11, 15, m(7), m(14))

    g(state, 0, 5, 10, 15, m(6), m(5))
    g(state, 1, 6, 11, 12, m(9), m(0))
    g(state, 2, 7, 8, 13, m(11), m(15))
    g(state, 3, 4, 9, 14, m(8), m(1))
  }

  @inline
  private def round_4(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(10), m(7))
    g(state, 1, 5, 9, 13, m(12), m(9))
    g(state, 2, 6, 10, 14, m(14), m(3))
    g(state, 3, 7, 11, 15, m(13), m(15))

    g(state, 0, 5, 10, 15, m(4), m(0))
    g(state, 1, 6, 11, 12, m(11), m(2))
    g(state, 2, 7, 8, 13, m(5), m(8))
    g(state, 3, 4, 9, 14, m(1), m(6))
  }

  @inline
  private def round_5(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(12), m(13))
    g(state, 1, 5, 9, 13, m(9), m(11))
    g(state, 2, 6, 10, 14, m(15), m(10))
    g(state, 3, 7, 11, 15, m(14), m(8))

    g(state, 0, 5, 10, 15, m(7), m(2))
    g(state, 1, 6, 11, 12, m(5), m(3))
    g(state, 2, 7, 8, 13, m(0), m(1))
    g(state, 3, 4, 9, 14, m(6), m(4))
  }

  @inline
  private def round_6(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(9), m(14))
    g(state, 1, 5, 9, 13, m(11), m(5))
    g(state, 2, 6, 10, 14, m(8), m(12))
    g(state, 3, 7, 11, 15, m(15), m(1))

    g(state, 0, 5, 10, 15, m(13), m(3))
    g(state, 1, 6, 11, 12, m(0), m(10))
    g(state, 2, 7, 8, 13, m(2), m(6))
    g(state, 3, 4, 9, 14, m(4), m(7))
  }

  @inline
  private def round_7(state: Array[Int], m: Array[Int]): Unit = {
    g(state, 0, 4, 8, 12, m(11), m(15))
    g(state, 1, 5, 9, 13, m(5), m(0))
    g(state, 2, 6, 10, 14, m(1), m(9))
    g(state, 3, 7, 11, 15, m(8), m(6))

    g(state, 0, 5, 10, 15, m(14), m(10))
    g(state, 1, 6, 11, 12, m(2), m(12))
    g(state, 2, 7, 8, 13, m(3), m(4))
    g(state, 3, 4, 9, 14, m(7), m(13))
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

    state(8) = IV(0)
    state(9) = IV(1)
    state(10) = IV(2)
    state(11) = IV(3)

    state(12) = counter.toInt
    state(13) = (counter >> 32).toInt
    state(14) = blockLen
    state(15) = flags

    round_1(state, blockWords)
    round_2(state, blockWords)
    round_3(state, blockWords)
    round_4(state, blockWords)
    round_5(state, blockWords)
    round_6(state, blockWords)
    round_7(state, blockWords)
  }

  def compressInPlace(
    chainingValue: Array[Int],
    bytes: Array[Byte],
    bytesOffset: Int,
    counter: Long,
    blockLen: Int,
    flags: Int,
    tmpState: Array[Int],
    tmpBlockWords: Array[Int]
  ): Unit = {

    tmpBlockWords(0) = ((bytes(3 + bytesOffset) & 0xff) << 24) |
      ((bytes(2 + bytesOffset) & 0xff) << 16) |
      ((bytes(1 + bytesOffset) & 0xff) << 8) |
      ((bytes(0 + bytesOffset) & 0xff))

    tmpBlockWords(1) = ((bytes(7 + bytesOffset) & 0xff) << 24) |
      ((bytes(6 + bytesOffset) & 0xff) << 16) |
      ((bytes(5 + bytesOffset) & 0xff) << 8) |
      ((bytes(4 + bytesOffset) & 0xff))

    tmpBlockWords(2) = ((bytes(11 + bytesOffset) & 0xff) << 24) |
      ((bytes(10 + bytesOffset) & 0xff) << 16) |
      ((bytes(9 + bytesOffset) & 0xff) << 8) |
      ((bytes(8 + bytesOffset) & 0xff))

    tmpBlockWords(3) = ((bytes(15 + bytesOffset) & 0xff) << 24) |
      ((bytes(14 + bytesOffset) & 0xff) << 16) |
      ((bytes(13 + bytesOffset) & 0xff) << 8) |
      ((bytes(12 + bytesOffset) & 0xff))

    tmpBlockWords(4) = ((bytes(19 + bytesOffset) & 0xff) << 24) |
      ((bytes(18 + bytesOffset) & 0xff) << 16) |
      ((bytes(17 + bytesOffset) & 0xff) << 8) |
      ((bytes(16 + bytesOffset) & 0xff))

    tmpBlockWords(5) = ((bytes(23 + bytesOffset) & 0xff) << 24) |
      ((bytes(22 + bytesOffset) & 0xff) << 16) |
      ((bytes(21 + bytesOffset) & 0xff) << 8) |
      ((bytes(20 + bytesOffset) & 0xff))

    tmpBlockWords(6) = ((bytes(27 + bytesOffset) & 0xff) << 24) |
      ((bytes(26 + bytesOffset) & 0xff) << 16) |
      ((bytes(25 + bytesOffset) & 0xff) << 8) |
      ((bytes(24 + bytesOffset) & 0xff))

    tmpBlockWords(7) = ((bytes(31 + bytesOffset) & 0xff) << 24) |
      ((bytes(30 + bytesOffset) & 0xff) << 16) |
      ((bytes(29 + bytesOffset) & 0xff) << 8) |
      ((bytes(28 + bytesOffset) & 0xff))

    tmpBlockWords(8) = ((bytes(35 + bytesOffset) & 0xff) << 24) |
      ((bytes(34 + bytesOffset) & 0xff) << 16) |
      ((bytes(33 + bytesOffset) & 0xff) << 8) |
      ((bytes(32 + bytesOffset) & 0xff))

    tmpBlockWords(9) = ((bytes(39 + bytesOffset) & 0xff) << 24) |
      ((bytes(38 + bytesOffset) & 0xff) << 16) |
      ((bytes(37 + bytesOffset) & 0xff) << 8) |
      ((bytes(36 + bytesOffset) & 0xff))

    tmpBlockWords(10) = ((bytes(43 + bytesOffset) & 0xff) << 24) |
      ((bytes(42 + bytesOffset) & 0xff) << 16) |
      ((bytes(41 + bytesOffset) & 0xff) << 8) |
      ((bytes(40 + bytesOffset) & 0xff))

    tmpBlockWords(11) = ((bytes(47 + bytesOffset) & 0xff) << 24) |
      ((bytes(46 + bytesOffset) & 0xff) << 16) |
      ((bytes(45 + bytesOffset) & 0xff) << 8) |
      ((bytes(44 + bytesOffset) & 0xff))

    tmpBlockWords(12) = ((bytes(51 + bytesOffset) & 0xff) << 24) |
      ((bytes(50 + bytesOffset) & 0xff) << 16) |
      ((bytes(49 + bytesOffset) & 0xff) << 8) |
      ((bytes(48 + bytesOffset) & 0xff))

    tmpBlockWords(13) = ((bytes(55 + bytesOffset) & 0xff) << 24) |
      ((bytes(54 + bytesOffset) & 0xff) << 16) |
      ((bytes(53 + bytesOffset) & 0xff) << 8) |
      ((bytes(52 + bytesOffset) & 0xff))

    tmpBlockWords(14) = ((bytes(59 + bytesOffset) & 0xff) << 24) |
      ((bytes(58 + bytesOffset) & 0xff) << 16) |
      ((bytes(57 + bytesOffset) & 0xff) << 8) |
      ((bytes(56 + bytesOffset) & 0xff))

    tmpBlockWords(15) = ((bytes(63 + bytesOffset) & 0xff) << 24) |
      ((bytes(62 + bytesOffset) & 0xff) << 16) |
      ((bytes(61 + bytesOffset) & 0xff) << 8) |
      ((bytes(60 + bytesOffset) & 0xff))

    compressRounds(tmpState, tmpBlockWords, chainingValue, counter, blockLen, flags)

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

  def wordsFromLittleEndianBytes(bytes: Array[Byte]): Array[Int] = {
    val res = new Array[Int](bytes.length / 4)
    var i = 0
    var off = 0
    while (i < res.length) {
      res(i) = ((bytes(3 + off) & 0xff) << 24) |
        ((bytes(2 + off) & 0xff) << 16) |
        ((bytes(1 + off) & 0xff) << 8) |
        ((bytes(off) & 0xff))
      i += 1
      off += 4
    }
    res
  }

  def mergeChildCV(leftChildCV: Array[Int], rightChildCv: Array[Int]): Array[Int] = {
    val merged = new Array[Int](16)
    System.arraycopy(leftChildCV, 0, merged, 0, KEY_LEN_WORDS)
    System.arraycopy(rightChildCv, 0, merged, KEY_LEN_WORDS, KEY_LEN_WORDS)
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
