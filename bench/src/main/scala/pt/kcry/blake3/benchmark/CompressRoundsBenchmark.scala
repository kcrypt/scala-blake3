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
package benchmark

import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class CompressRoundsBenchmark {
  private val state = new Array[Int](BLOCK_LEN_WORDS)
  private val blockWords = new Array[Int](BLOCK_LEN_WORDS)
  private val chainingValue = new Array[Int](BLOCK_LEN_WORDS)

  private var counter: Long = 0
  private var blockLen: Int = 0
  private var flags: Int = 0

  @Setup
  def setup(): Unit = {
    val random = new Random()
    for (i <- 0 until BLOCK_LEN_WORDS) {
      state(i) = random.nextInt()
      blockWords(i) = random.nextInt()
      chainingValue(i) = random.nextInt()
    }

    counter = random.nextLong()
    blockLen = random.nextInt()
    flags = random.nextInt()
  }

  @Benchmark
  def inline(): Unit =
    inlineImpl(state, blockWords, chainingValue, counter, blockLen, flags)

  @Benchmark
  def withoutInline(): Unit = compressRoundsImpl(
    state, blockWords, chainingValue, counter, blockLen, flags
  )

  @Benchmark
  def mArrays(): Unit =
    mArraysImpl(state, blockWords, chainingValue, counter, blockLen, flags)

  @Benchmark
  def gs(): Unit = compressRoundsGsImpl(
    state, blockWords, chainingValue, counter, blockLen, flags
  )

  @Benchmark
  def gOnlyInline(): Unit = gOnlyInlineCompressRoundsImpl(
    state, blockWords, chainingValue, counter, blockLen, flags
  )

  private def inlineImpl(
    state: Array[Int], blockWords: Array[Int], chainingValue: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
    var state_4 = chainingValue(4)
    var state_5 = chainingValue(5)
    var state_6 = chainingValue(6)
    var state_7 = chainingValue(7)

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
    var state_0 = chainingValue(0) + state_4 + m_0
    var state_12 = counter.toInt ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    var state_8 = 0x6a09e667 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_1
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    var state_1 = chainingValue(1) + state_5 + m_2
    var state_13 = (counter >> 32).toInt ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    var state_9 = 0xbb67ae85 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_3
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    var state_2 = chainingValue(2) + state_6 + m_4
    var state_14 = blockLen ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    var state_10 = 0x3c6ef372 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_5
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    var state_3 = chainingValue(3) + state_7 + m_6
    var state_15 = flags ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    var state_11 = 0xa54ff53a + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_7
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_8
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_9
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_10
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_11
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_12
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_13
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_14
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_15
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // round 2
    state_0 = state_0 + state_4 + m_2
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_6
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    state_1 = state_1 + state_5 + m_3
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_10
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_2 = state_2 + state_6 + m_7
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_0
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_3 = state_3 + state_7 + m_4
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_13
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_1
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_11
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_12
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_5
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_9
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_14
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_15
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_8
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // round 3
    state_0 = state_0 + state_4 + m_3
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_4
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    state_1 = state_1 + state_5 + m_10
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_12
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_2 = state_2 + state_6 + m_13
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_2
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_3 = state_3 + state_7 + m_7
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_14
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_6
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_5
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_9
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_0
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_11
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_15
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_8
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_1
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // round 4
    state_0 = state_0 + state_4 + m_10
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_7
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    state_1 = state_1 + state_5 + m_12
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_9
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_2 = state_2 + state_6 + m_14
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_3
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_3 = state_3 + state_7 + m_13
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_15
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_4
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_0
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_11
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_2
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_5
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_8
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_1
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_6
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // round 5
    state_0 = state_0 + state_4 + m_12
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_13
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    state_1 = state_1 + state_5 + m_9
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_11
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_2 = state_2 + state_6 + m_15
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_10
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_3 = state_3 + state_7 + m_14
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_8
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_7
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_2
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_5
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_3
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_0
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_1
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_6
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_4
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // round 6
    state_0 = state_0 + state_4 + m_9
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_14
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    state_1 = state_1 + state_5 + m_11
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_5
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_2 = state_2 + state_6 + m_8
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_12
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_3 = state_3 + state_7 + m_15
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_1
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_13
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_3
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_0
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_10
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_2
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_6
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_4
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_7
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // round 7
    state_0 = state_0 + state_4 + m_11
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_15
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    state_1 = state_1 + state_5 + m_5
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_0
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_2 = state_2 + state_6 + m_1
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_9
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_3 = state_3 + state_7 + m_8
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_6
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_0 = state_0 + state_5 + m_14
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_10
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    state_1 = state_1 + state_6 + m_2
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_12
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    state_2 = state_2 + state_7 + m_3
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_4
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    state_3 = state_3 + state_4 + m_7
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_13
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

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

  @inline
  private def g(
    state: Array[Int], a: Int, b: Int, c: Int, d: Int, mx: Int, my: Int
  ): Unit = {
    var state_b = state(b)
    var state_d = state(d)

    var state_a = state(a) + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    var state_c = state(c) + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state(b) = (state_b >>> 7) | (state_b << 25)

    state(a) = state_a
    state(c) = state_c
    state(d) = state_d
  }

  @inline
  private def round(
    state: Array[Int], m_0: Int, m_1: Int, m_2: Int, m_3: Int, m_4: Int,
    m_5: Int, m_6: Int, m_7: Int, m_8: Int, m_9: Int, m_10: Int, m_11: Int,
    m_12: Int, m_13: Int, m_14: Int, m_15: Int
  ): Unit = {
    g(state, 0, 4, 8, 12, m_0, m_1)
    g(state, 1, 5, 9, 13, m_2, m_3)
    g(state, 2, 6, 10, 14, m_4, m_5)
    g(state, 3, 7, 11, 15, m_6, m_7)

    g(state, 0, 5, 10, 15, m_8, m_9)
    g(state, 1, 6, 11, 12, m_10, m_11)
    g(state, 2, 7, 8, 13, m_12, m_13)
    g(state, 3, 4, 9, 14, m_14, m_15)
  }

  @inline
  private def compressRoundsImpl(
    state: Array[Int], blockWords: Array[Int], chainingValue: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
    // CV 0..7
    System.arraycopy(chainingValue, 0, state, 0, 8)
    System.arraycopy(IV, 0, state, 8, 4)

    state(12) = counter.toInt
    state(13) = (counter >> 32).toInt
    state(14) = blockLen
    state(15) = flags

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
    round(
      state, m_0, m_1, m_2, m_3, m_4, m_5, m_6, m_7, m_8, m_9, m_10, m_11, m_12,
      m_13, m_14, m_15
    )

    // round 2
    round(
      state, m_2, m_6, m_3, m_10, m_7, m_0, m_4, m_13, m_1, m_11, m_12, m_5,
      m_9, m_14, m_15, m_8
    )

    // round 3
    round(
      state, m_3, m_4, m_10, m_12, m_13, m_2, m_7, m_14, m_6, m_5, m_9, m_0,
      m_11, m_15, m_8, m_1
    )

    // round 4
    round(
      state, m_10, m_7, m_12, m_9, m_14, m_3, m_13, m_15, m_4, m_0, m_11, m_2,
      m_5, m_8, m_1, m_6
    )

    // round 5
    round(
      state, m_12, m_13, m_9, m_11, m_15, m_10, m_14, m_8, m_7, m_2, m_5, m_3,
      m_0, m_1, m_6, m_4
    )

    // round 6
    round(
      state, m_9, m_14, m_11, m_5, m_8, m_12, m_15, m_1, m_13, m_3, m_0, m_10,
      m_2, m_6, m_4, m_7
    )

    // round 7
    round(
      state, m_11, m_15, m_5, m_0, m_1, m_9, m_8, m_6, m_14, m_10, m_2, m_12,
      m_3, m_4, m_7, m_13
    )

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

  @inline
  private def gInlineRound(
    state: Array[Int], m_0: Int, m_1: Int, m_2: Int, m_3: Int, m_4: Int,
    m_5: Int, m_6: Int, m_7: Int, m_8: Int, m_9: Int, m_10: Int, m_11: Int,
    m_12: Int, m_13: Int, m_14: Int, m_15: Int
  ): Unit = {
    var state_0 = state(0)
    var state_1 = state(1)
    var state_2 = state(2)
    var state_3 = state(3)
    var state_4 = state(4)
    var state_5 = state(5)
    var state_6 = state(6)
    var state_7 = state(7)
    var state_8 = state(8)
    var state_9 = state(9)
    var state_10 = state(10)
    var state_11 = state(11)
    var state_12 = state(12)
    var state_13 = state(13)
    var state_14 = state(14)
    var state_15 = state(15)

    // g(state, 0, 4, 8, 12, m_0, m_1)
    state_0 = state_0 + state_4 + m_0
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_0 = state_0 + state_4 + m_1
    state_12 = state_12 ^ state_0
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_8 = state_8 + state_12
    state_4 = state_4 ^ state_8
    state_4 = (state_4 >>> 7) | (state_4 << 25)

    // g(state, 1, 5, 9, 13, m_2, m_3)
    state_1 = state_1 + state_5 + m_2
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_1 = state_1 + state_5 + m_3
    state_13 = state_13 ^ state_1
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_9 = state_9 + state_13
    state_5 = state_5 ^ state_9
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    // g(state, 2, 6, 10, 14, m_4, m_5)
    state_2 = state_2 + state_6 + m_4
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_2 = state_2 + state_6 + m_5
    state_14 = state_14 ^ state_2
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_10 = state_10 + state_14
    state_6 = state_6 ^ state_10
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    // g(state, 3, 7, 11, 15, m_6, m_7)
    state_3 = state_3 + state_7 + m_6
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_3 = state_3 + state_7 + m_7
    state_15 = state_15 ^ state_3
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_11 = state_11 + state_15
    state_7 = state_7 ^ state_11
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    // g(state, 0, 5, 10, 15, m_8, m_9)
    state_0 = state_0 + state_5 + m_8
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 16) | (state_15 << 16)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 12) | (state_5 << 20)

    state_0 = state_0 + state_5 + m_9
    state_15 = state_15 ^ state_0
    state_15 = (state_15 >>> 8) | (state_15 << 24)

    state_10 = state_10 + state_15
    state_5 = state_5 ^ state_10
    state_5 = (state_5 >>> 7) | (state_5 << 25)

    // g(state, 1, 6, 11, 12, m_10, m_11)
    state_1 = state_1 + state_6 + m_10
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 16) | (state_12 << 16)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 12) | (state_6 << 20)

    state_1 = state_1 + state_6 + m_11
    state_12 = state_12 ^ state_1
    state_12 = (state_12 >>> 8) | (state_12 << 24)

    state_11 = state_11 + state_12
    state_6 = state_6 ^ state_11
    state_6 = (state_6 >>> 7) | (state_6 << 25)

    // g(state, 2, 7, 8, 13, m_12, m_13)
    state_2 = state_2 + state_7 + m_12
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 16) | (state_13 << 16)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 12) | (state_7 << 20)

    state_2 = state_2 + state_7 + m_13
    state_13 = state_13 ^ state_2
    state_13 = (state_13 >>> 8) | (state_13 << 24)

    state_8 = state_8 + state_13
    state_7 = state_7 ^ state_8
    state_7 = (state_7 >>> 7) | (state_7 << 25)

    // g(state, 3, 4, 9, 14, m_14, m_15)
    state_3 = state_3 + state_4 + m_14
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 16) | (state_14 << 16)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 12) | (state_4 << 20)

    state_3 = state_3 + state_4 + m_15
    state_14 = state_14 ^ state_3
    state_14 = (state_14 >>> 8) | (state_14 << 24)

    state_9 = state_9 + state_14
    state_4 = state_4 ^ state_9
    state_4 = (state_4 >>> 7) | (state_4 << 25)

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

  def gOnlyInlineCompressRoundsImpl(
    state: Array[Int], blockWords: Array[Int], chainingValue: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
    // CV 0..7
    System.arraycopy(chainingValue, 0, state, 0, 8)
    System.arraycopy(IV, 0, state, 8, 4)

    state(12) = counter.toInt
    state(13) = (counter >> 32).toInt
    state(14) = blockLen
    state(15) = flags

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
    gInlineRound(
      state, m_0, m_1, m_2, m_3, m_4, m_5, m_6, m_7, m_8, m_9, m_10, m_11, m_12,
      m_13, m_14, m_15
    )

    // round 2
    gInlineRound(
      state, m_2, m_6, m_3, m_10, m_7, m_0, m_4, m_13, m_1, m_11, m_12, m_5,
      m_9, m_14, m_15, m_8
    )

    // round 3
    gInlineRound(
      state, m_3, m_4, m_10, m_12, m_13, m_2, m_7, m_14, m_6, m_5, m_9, m_0,
      m_11, m_15, m_8, m_1
    )

    // round 4
    gInlineRound(
      state, m_10, m_7, m_12, m_9, m_14, m_3, m_13, m_15, m_4, m_0, m_11, m_2,
      m_5, m_8, m_1, m_6
    )

    // round 5
    gInlineRound(
      state, m_12, m_13, m_9, m_11, m_15, m_10, m_14, m_8, m_7, m_2, m_5, m_3,
      m_0, m_1, m_6, m_4
    )

    // round 6
    gInlineRound(
      state, m_9, m_14, m_11, m_5, m_8, m_12, m_15, m_1, m_13, m_3, m_0, m_10,
      m_2, m_6, m_4, m_7
    )

    // round 7
    gInlineRound(
      state, m_11, m_15, m_5, m_0, m_1, m_9, m_8, m_6, m_14, m_10, m_2, m_12,
      m_3, m_4, m_7, m_13
    )

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

  @inline
  private def mArraysRound(
    state: Array[Int], blockWords: Array[Int], m: Array[Int]
  ): Unit = {
    g(state, 0, 4, 8, 12, blockWords(m(0)), blockWords(m(1)))
    g(state, 1, 5, 9, 13, blockWords(m(2)), blockWords(m(3)))
    g(state, 2, 6, 10, 14, blockWords(m(4)), blockWords(m(5)))
    g(state, 3, 7, 11, 15, blockWords(m(6)), blockWords(m(7)))

    g(state, 0, 5, 10, 15, blockWords(m(8)), blockWords(m(9)))
    g(state, 1, 6, 11, 12, blockWords(m(10)), blockWords(m(11)))
    g(state, 2, 7, 8, 13, blockWords(m(12)), blockWords(m(13)))
    g(state, 3, 4, 9, 14, blockWords(m(14)), blockWords(m(15)))
  }

  private val M_R1 =
    Array[Int](0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
  private val M_R2 =
    Array[Int](2, 6, 3, 10, 7, 0, 4, 13, 1, 11, 12, 5, 9, 14, 15, 8)
  private val M_R3 =
    Array[Int](3, 4, 10, 12, 13, 2, 7, 14, 6, 5, 9, 0, 11, 15, 8, 1)
  private val M_R4 =
    Array[Int](10, 7, 12, 9, 14, 3, 13, 15, 4, 0, 11, 2, 5, 8, 1, 6)
  private val M_R5 =
    Array[Int](12, 13, 9, 11, 15, 10, 14, 8, 7, 2, 5, 3, 0, 1, 6, 4)
  private val M_R6 =
    Array[Int](9, 14, 11, 5, 8, 12, 15, 1, 13, 3, 0, 10, 2, 6, 4, 7)
  private val M_R7 =
    Array[Int](11, 15, 5, 0, 1, 9, 8, 6, 14, 10, 2, 12, 3, 4, 7, 13)

  @inline
  private def mArraysImpl(
    state: Array[Int], blockWords: Array[Int], chainingValue: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
    // CV 0..7
    System.arraycopy(chainingValue, 0, state, 0, 8)
    System.arraycopy(IV, 0, state, 8, 4)

    state(12) = counter.toInt
    state(13) = (counter >> 32).toInt
    state(14) = blockLen
    state(15) = flags

    // round 1
    mArraysRound(state, blockWords, M_R1)

    // round 2
    mArraysRound(state, blockWords, M_R2)

    // round 3
    mArraysRound(state, blockWords, M_R3)

    // round 4
    mArraysRound(state, blockWords, M_R4)

    // round 5
    mArraysRound(state, blockWords, M_R5)

    // round 6
    mArraysRound(state, blockWords, M_R6)

    // round 7
    mArraysRound(state, blockWords, M_R7)

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

  @inline
  private def g1(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(0)
    var state_b = state(4)
    var state_c = state(8)
    var state_d = state(12)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(0) = state_a
    state(4) = state_b
    state(8) = state_c
    state(12) = state_d
  }

  @inline
  private def g2(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(1)
    var state_b = state(5)
    var state_c = state(9)
    var state_d = state(13)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(1) = state_a
    state(5) = state_b
    state(9) = state_c
    state(13) = state_d
  }

  @inline
  private def g3(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(2)
    var state_b = state(6)
    var state_c = state(10)
    var state_d = state(14)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(2) = state_a
    state(6) = state_b
    state(10) = state_c
    state(14) = state_d
  }

  @inline
  private def g4(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(3)
    var state_b = state(7)
    var state_c = state(11)
    var state_d = state(15)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(3) = state_a
    state(7) = state_b
    state(11) = state_c
    state(15) = state_d
  }

  @inline
  private def g5(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(0)
    var state_b = state(5)
    var state_c = state(11)
    var state_d = state(12)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(0) = state_a
    state(5) = state_b
    state(11) = state_c
    state(12) = state_d
  }

  @inline
  private def g6(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(1)
    var state_b = state(6)
    var state_c = state(11)
    var state_d = state(12)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(1) = state_a
    state(6) = state_b
    state(11) = state_c
    state(12) = state_d
  }

  @inline
  private def g7(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(3)
    var state_b = state(7)
    var state_c = state(8)
    var state_d = state(13)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(3) = state_a
    state(7) = state_b
    state(8) = state_c
    state(13) = state_d
  }

  @inline
  private def g8(state: Array[Int], mx: Int, my: Int): Unit = {
    var state_a = state(3)
    var state_b = state(4)
    var state_c = state(9)
    var state_d = state(14)

    state_a = state_a + state_b + mx
    state_d = state_d ^ state_a
    state_d = (state_d >>> 16) | (state_d << 16)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 12) | (state_b << 20)

    state_a = state_a + state_b + my
    state_d = state_d ^ state_a
    state_d = (state_d >>> 8) | (state_d << 24)

    state_c = state_c + state_d
    state_b = state_b ^ state_c
    state_b = (state_b >>> 7) | (state_b << 25)

    state(3) = state_a
    state(4) = state_b
    state(9) = state_c
    state(14) = state_d
  }

  @inline
  private def roundGs(
    state: Array[Int], m_0: Int, m_1: Int, m_2: Int, m_3: Int, m_4: Int,
    m_5: Int, m_6: Int, m_7: Int, m_8: Int, m_9: Int, m_10: Int, m_11: Int,
    m_12: Int, m_13: Int, m_14: Int, m_15: Int
  ): Unit = {
    g1(state, m_0, m_1)
    g2(state, m_2, m_3)
    g3(state, m_4, m_5)
    g4(state, m_6, m_7)

    g5(state, m_8, m_9)
    g6(state, m_10, m_11)
    g7(state, m_12, m_13)
    g8(state, m_14, m_15)
  }

  @inline
  private def compressRoundsGsImpl(
    state: Array[Int], blockWords: Array[Int], chainingValue: Array[Int],
    counter: Long, blockLen: Int, flags: Int
  ): Unit = {
    // CV 0..7
    System.arraycopy(chainingValue, 0, state, 0, 8)
    System.arraycopy(IV, 0, state, 8, 4)

    state(12) = counter.toInt
    state(13) = (counter >> 32).toInt
    state(14) = blockLen
    state(15) = flags

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
    roundGs(
      state, m_0, m_1, m_2, m_3, m_4, m_5, m_6, m_7, m_8, m_9, m_10, m_11, m_12,
      m_13, m_14, m_15
    )

    // round 2
    roundGs(
      state, m_2, m_6, m_3, m_10, m_7, m_0, m_4, m_13, m_1, m_11, m_12, m_5,
      m_9, m_14, m_15, m_8
    )

    // round 3
    roundGs(
      state, m_3, m_4, m_10, m_12, m_13, m_2, m_7, m_14, m_6, m_5, m_9, m_0,
      m_11, m_15, m_8, m_1
    )

    // round 4
    roundGs(
      state, m_10, m_7, m_12, m_9, m_14, m_3, m_13, m_15, m_4, m_0, m_11, m_2,
      m_5, m_8, m_1, m_6
    )

    // round 5
    roundGs(
      state, m_12, m_13, m_9, m_11, m_15, m_10, m_14, m_8, m_7, m_2, m_5, m_3,
      m_0, m_1, m_6, m_4
    )

    // round 6
    roundGs(
      state, m_9, m_14, m_11, m_5, m_8, m_12, m_15, m_1, m_13, m_3, m_0, m_10,
      m_2, m_6, m_4, m_7
    )

    // round 7
    roundGs(
      state, m_11, m_15, m_5, m_0, m_1, m_9, m_8, m_6, m_14, m_10, m_2, m_12,
      m_3, m_4, m_7, m_13
    )

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
}
