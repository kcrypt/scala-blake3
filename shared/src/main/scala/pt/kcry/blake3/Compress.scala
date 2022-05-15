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
import CompressTmpBlockWords._

private[blake3] object Compress {
  def compressInPlace(
    chainingValue: Array[Int], bytes: Array[Byte], bytesOffset: Int,
    counter: Long, blockLen: Int, flags: Int, tmpState: Array[Int],
    tmpBlockWords: Array[Int]
  ): Unit = {
    compressTmpBlockWords(bytes, bytesOffset, tmpBlockWords)

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
}
