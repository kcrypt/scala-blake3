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

private[blake3] object CompressRounds {
  def compressRounds(
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
    state(0) = state_0 ^ state_8
    state(1) = state_1 ^ state_9
    state(2) = state_2 ^ state_10
    state(3) = state_3 ^ state_11
    state(4) = state_4 ^ state_12
    state(5) = state_5 ^ state_13
    state(6) = state_6 ^ state_14
    state(7) = state_7 ^ state_15
    state(8) = state_8 ^ chainingValue(0)
    state(9) = state_9 ^ chainingValue(1)
    state(10) = state_10 ^ chainingValue(2)
    state(11) = state_11 ^ chainingValue(3)
    state(12) = state_12 ^ chainingValue(4)
    state(13) = state_13 ^ chainingValue(5)
    state(14) = state_14 ^ chainingValue(6)
    state(15) = state_15 ^ chainingValue(7)
  }
}
