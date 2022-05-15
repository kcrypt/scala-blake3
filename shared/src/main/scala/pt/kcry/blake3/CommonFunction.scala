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

private[blake3] object CommonFunction {
  @inline
  private def compressRounds(
    state: Array[Int], blockWords: Array[Int], chainingValue: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
    // CV 0..7
    var state_0 = chainingValue(0)
    var state_1 = chainingValue(1)
    var state_2 = chainingValue(2)
    var state_3 = chainingValue(3)
    var state_4 = chainingValue(4)
    var state_5 = chainingValue(5)
    var state_6 = chainingValue(6)
    var state_7 = chainingValue(7)

    // constants from IV
    var state_8 = 0x6a09e667
    var state_9 = 0xbb67ae85
    var state_10 = 0x3c6ef372
    var state_11 = 0xa54ff53a

    // noise
    var state_12 = counter.toInt
    var state_13 = (counter >> 32).toInt
    var state_14 = blockLen
    var state_15 = flags

    val m_0 = blockWords(0)
    val m_1 = blockWords(1)
    val m_2 = blockWords(2)
    val m_3 = blockWords(3)
    val m_4 = blockWords(4)
    val m_5 = blockWords(5)
    val m_6 = blockWords(6)
    val m_7 = blockWords(7)
    val m_8 = blockWords(8)
    val m_9 = blockWords(9)
    val m_10 = blockWords(10)
    val m_11 = blockWords(11)
    val m_12 = blockWords(12)
    val m_13 = blockWords(13)
    val m_14 = blockWords(14)
    val m_15 = blockWords(15)

    // round 1
    state_0 = state_0 + state_4 + m_0
    var `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    var `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_1
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_2
    var `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    var `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_3
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_4
    var `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    var `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_5
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_6
    var `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    var `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_7
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_8
    var `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    var `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_9
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_10
    var `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    var `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_11
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_12
    var `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    var `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_13
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_14
    var `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    var `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_15
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // round 2
    state_0 = state_0 + state_4 + m_2
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_6
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_3
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_10
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_7
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_0
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_4
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_13
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_1
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_11
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_12
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_5
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_9
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_14
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_15
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_8
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // round 3
    state_0 = state_0 + state_4 + m_3
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_4
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_10
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_12
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_13
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_2
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_7
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_14
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_6
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_5
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_9
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_0
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_11
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_15
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_8
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_1
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // round 4
    state_0 = state_0 + state_4 + m_10
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_7
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_12
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_9
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_14
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_3
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_13
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_15
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_4
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_0
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_11
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_2
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_5
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_8
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_1
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_6
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // round 5
    state_0 = state_0 + state_4 + m_12
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_13
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_9
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_11
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_15
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_10
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_14
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_8
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_7
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_2
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_5
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_3
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_0
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_1
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_6
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_4
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // round 6
    state_0 = state_0 + state_4 + m_9
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_14
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_11
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_5
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_8
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_12
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_15
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_1
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_13
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_3
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_0
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_10
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_2
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_6
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_4
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_7
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // round 7
    state_0 = state_0 + state_4 + m_11
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 16) | (`state_12 ^ state_0` << 16)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 12) | (`state_4 ^ state_8` << 20)

    state_0 = state_0 + state_4 + m_15
    `state_12 ^ state_0` = state_12 ^ state_0
    state_12 = (`state_12 ^ state_0` >>> 8) | (`state_12 ^ state_0` << 24)

    state_8 = state_8 + state_12
    `state_4 ^ state_8` = state_4 ^ state_8
    state_4 = (`state_4 ^ state_8` >>> 7) | (`state_4 ^ state_8` << 25)

    state_1 = state_1 + state_5 + m_5
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 16) | (`state_13 ^ state_1` << 16)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 12) | (`state_5 ^ state_9` << 20)

    state_1 = state_1 + state_5 + m_0
    `state_13 ^ state_1` = state_13 ^ state_1
    state_13 = (`state_13 ^ state_1` >>> 8) | (`state_13 ^ state_1` << 24)

    state_9 = state_9 + state_13
    `state_5 ^ state_9` = state_5 ^ state_9
    state_5 = (`state_5 ^ state_9` >>> 7) | (`state_5 ^ state_9` << 25)

    state_2 = state_2 + state_6 + m_1
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 16) | (`state_14 ^ state_2` << 16)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 12) | (`state_6 ^ state_10` << 20)

    state_2 = state_2 + state_6 + m_9
    `state_14 ^ state_2` = state_14 ^ state_2
    state_14 = (`state_14 ^ state_2` >>> 8) | (`state_14 ^ state_2` << 24)

    state_10 = state_10 + state_14
    `state_6 ^ state_10` = state_6 ^ state_10
    state_6 = (`state_6 ^ state_10` >>> 7) | (`state_6 ^ state_10` << 25)

    state_3 = state_3 + state_7 + m_8
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 16) | (`state_15 ^ state_3` << 16)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 12) | (`state_7 ^ state_11` << 20)

    state_3 = state_3 + state_7 + m_6
    `state_15 ^ state_3` = state_15 ^ state_3
    state_15 = (`state_15 ^ state_3` >>> 8) | (`state_15 ^ state_3` << 24)

    state_11 = state_11 + state_15
    `state_7 ^ state_11` = state_7 ^ state_11
    state_7 = (`state_7 ^ state_11` >>> 7) | (`state_7 ^ state_11` << 25)

    state_0 = state_0 + state_5 + m_14
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 16) | (`state_15 ^ state_0` << 16)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 12) | (`state_5 ^ state_10` << 20)

    state_0 = state_0 + state_5 + m_10
    `state_15 ^ state_0` = state_15 ^ state_0
    state_15 = (`state_15 ^ state_0` >>> 8) | (`state_15 ^ state_0` << 24)

    state_10 = state_10 + state_15
    `state_5 ^ state_10` = state_5 ^ state_10
    state_5 = (`state_5 ^ state_10` >>> 7) | (`state_5 ^ state_10` << 25)

    state_1 = state_1 + state_6 + m_2
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 16) | (`state_12 ^ state_1` << 16)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 12) | (`state_6 ^ state_11` << 20)

    state_1 = state_1 + state_6 + m_12
    `state_12 ^ state_1` = state_12 ^ state_1
    state_12 = (`state_12 ^ state_1` >>> 8) | (`state_12 ^ state_1` << 24)

    state_11 = state_11 + state_12
    `state_6 ^ state_11` = state_6 ^ state_11
    state_6 = (`state_6 ^ state_11` >>> 7) | (`state_6 ^ state_11` << 25)

    state_2 = state_2 + state_7 + m_3
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 16) | (`state_13 ^ state_2` << 16)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 12) | (`state_7 ^ state_8` << 20)

    state_2 = state_2 + state_7 + m_4
    `state_13 ^ state_2` = state_13 ^ state_2
    state_13 = (`state_13 ^ state_2` >>> 8) | (`state_13 ^ state_2` << 24)

    state_8 = state_8 + state_13
    `state_7 ^ state_8` = state_7 ^ state_8
    state_7 = (`state_7 ^ state_8` >>> 7) | (`state_7 ^ state_8` << 25)

    state_3 = state_3 + state_4 + m_7
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 16) | (`state_14 ^ state_3` << 16)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 12) | (`state_4 ^ state_9` << 20)

    state_3 = state_3 + state_4 + m_13
    `state_14 ^ state_3` = state_14 ^ state_3
    state_14 = (`state_14 ^ state_3` >>> 8) | (`state_14 ^ state_3` << 24)

    state_9 = state_9 + state_14
    `state_4 ^ state_9` = state_4 ^ state_9
    state_4 = (`state_4 ^ state_9` >>> 7) | (`state_4 ^ state_9` << 25)

    // update state
    state(0) = state_0
    state(1) = state_1
    state(2) = state_2
    state(3) = state_3
    state(4) = state_4
    state(5) = state_5
    state(6) = state_6
    state(7) = state_7
    state(8) = state_8
    state(9) = state_9
    state(10) = state_10
    state(11) = state_11
    state(12) = state_12
    state(13) = state_13
    state(14) = state_14
    state(15) = state_15
  }

  def compressInPlace(
    chainingValue: Array[Int], bytes: Array[Byte], bytesOffset: Int,
    counter: Long, blockLen: Int, flags: Int, tmpState: Array[Int],
    tmpBlockWords: Array[Int]
  ): Unit = {

    tmpBlockWords(0) = ((bytes(3 + bytesOffset) & 0xff) << 24) |
      ((bytes(2 + bytesOffset) & 0xff) << 16) |
      ((bytes(1 + bytesOffset) & 0xff) << 8) | ((bytes(0 + bytesOffset) & 0xff))

    tmpBlockWords(1) = ((bytes(7 + bytesOffset) & 0xff) << 24) |
      ((bytes(6 + bytesOffset) & 0xff) << 16) |
      ((bytes(5 + bytesOffset) & 0xff) << 8) | ((bytes(4 + bytesOffset) & 0xff))

    tmpBlockWords(2) = ((bytes(11 + bytesOffset) & 0xff) << 24) |
      ((bytes(10 + bytesOffset) & 0xff) << 16) |
      ((bytes(9 + bytesOffset) & 0xff) << 8) | ((bytes(8 + bytesOffset) & 0xff))

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

    compressRounds(tmpState, tmpBlockWords, chainingValue, counter, blockLen,
      flags)

    chainingValue(0) = tmpState(0) ^ tmpState(8)
    chainingValue(1) = tmpState(1) ^ tmpState(9)
    chainingValue(2) = tmpState(2) ^ tmpState(10)
    chainingValue(3) = tmpState(3) ^ tmpState(11)
    chainingValue(4) = tmpState(4) ^ tmpState(12)
    chainingValue(5) = tmpState(5) ^ tmpState(13)
    chainingValue(6) = tmpState(6) ^ tmpState(14)
    chainingValue(7) = tmpState(7) ^ tmpState(15)

  }

  def compressInPlace(
    state: Array[Int], chainingValue: Array[Int], blockWords: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
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
  }

  def compressSingle(
    chainingValue: Array[Int], blockWords: Array[Int], counter: Long,
    blockLen: Int, flags: Int
  ): Int = {
    val state = new Array[Int](BLOCK_LEN_WORDS)

    compressRounds(state, blockWords, chainingValue, counter, blockLen, flags)

    // a fast-track for single int
    state(0) ^ state(8)
  }

  def compressSingleLong(
    chainingValue: Array[Int], blockWords: Array[Int], counter: Long,
    blockLen: Int, flags: Int
  ): Long = {
    val state = new Array[Int](BLOCK_LEN_WORDS)

    compressRounds(state, blockWords, chainingValue, counter, blockLen, flags)

    // a fast-track for single long
    ((state(0) ^ state(8)).toLong << 32) | (state(1) ^ state(9))
  }
  def wordsFromLittleEndianBytes(bytes: Array[Byte]): Array[Int] = {
    val res = new Array[Int](bytes.length / 4)
    var i = 0
    var off = 0
    while (i < res.length) {
      res(i) = ((bytes(3 + off) & 0xff) << 24) |
        ((bytes(2 + off) & 0xff) << 16) | ((bytes(1 + off) & 0xff) << 8) |
        ((bytes(off) & 0xff))
      i += 1
      off += 4
    }
    res
  }

  @inline
  private def mergeChildCV(
    merged: Array[Int], leftChildCV: Array[Int], rightChildCv: Array[Int]
  ): Unit = {
    System.arraycopy(rightChildCv, 0, merged, KEY_LEN_WORDS, KEY_LEN_WORDS)
    System.arraycopy(leftChildCV, 0, merged, 0, KEY_LEN_WORDS)
  }

  def parentOutput(
    blockWords: Array[Int], leftChildCV: Array[Int], rightChildCv: Array[Int],
    key: Array[Int], flags: Int
  ): Output = {
    mergeChildCV(blockWords, leftChildCV, rightChildCv)
    new Output(key, blockWords, 0, BLOCK_LEN, flags | PARENT)
  }

  def parentCV(
    parentCV: Array[Int], leftChildCV: Array[Int], rightChildCv: Array[Int],
    key: Array[Int], flags: Int, tmpBlockWords: Array[Int]
  ): Unit = {
    mergeChildCV(tmpBlockWords, leftChildCV, rightChildCv)
    compressInPlace(parentCV, key, tmpBlockWords, 0, BLOCK_LEN, flags | PARENT)
  }

}
