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

package pt.kcry

package object blake3 {
  // constants
  val OUT_LEN: Int = 32
  val KEY_LEN: Int = 32
  val KEY_LEN_WORDS: Int = 8
  val BLOCK_LEN: Int = 64
  val BLOCK_LEN_WORDS: Int = 16
  val CHUNK_LEN: Int = 1024
  val MAX_DEPTH: Int = 54

  private[blake3] val CHUNK_START = 1 << 0
  private[blake3] val CHUNK_END = 1 << 1
  private[blake3] val PARENT = 1 << 2
  private[blake3] val ROOT = 1 << 3
  private[blake3] val KEYED_HASH = 1 << 4
  private[blake3] val DERIVE_KEY_CONTEXT = 1 << 5
  private[blake3] val DERIVE_KEY_MATERIAL = 1 << 6

  private[blake3] val IV = Array(0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
    0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19)
}
