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

private[blake3] object Compress {
  def compressInPlace(
    state: Array[Int], chainingValue: Array[Int], blockWords: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = compressRounds(state, blockWords, chainingValue, counter, blockLen,
    flags)

  def compressSingle(
    chainingValue: Array[Int], blockWords: Array[Int], counter: Long,
    blockLen: Int, flags: Int
  ): Int = {
    val state = new Array[Int](BLOCK_LEN_WORDS)

    compressRounds(state, blockWords, chainingValue, counter, blockLen, flags)

    // a fast-track for single int
    state(0)
  }

  def compressSingleLong(
    chainingValue: Array[Int], blockWords: Array[Int], counter: Long,
    blockLen: Int, flags: Int
  ): Long = {
    val state = new Array[Int](BLOCK_LEN_WORDS)

    compressRounds(state, blockWords, chainingValue, counter, blockLen, flags)

    // a fast-track for single long
    (state(0).toLong << 32) | state(1)
  }
}
