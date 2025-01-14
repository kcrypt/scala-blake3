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
class CompressBlockBenchmark {
  private val bytes = new Array[Byte](CHUNK_LEN)
  private val bytesOffset = 7

  private val tmpBlockWords = new Array[Int](BLOCK_LEN_WORDS)

  @Setup
  def setup(): Unit = {
    val random = new Random()
    random.nextBytes(bytes)
  }

  @Benchmark
  def inline(): Unit = inlineImpl(bytes, bytesOffset, tmpBlockWords)

  @Benchmark
  def loop(): Unit = loopImpl(bytes, bytesOffset, tmpBlockWords)

  @Benchmark
  def loop2(): Unit = loop2Impl(bytes, bytesOffset, tmpBlockWords)

  private def inlineImpl(
      bytes: Array[Byte], bytesOffset: Int, tmpBlockWords: Array[Int]
  ): Unit = {
    tmpBlockWords(0) = ((bytes(3 + bytesOffset) & 0xff) << 24) |
      ((bytes(2 + bytesOffset) & 0xff) << 16) |
      ((bytes(1 + bytesOffset) & 0xff) << 8) | bytes(0 + bytesOffset) & 0xff

    tmpBlockWords(1) = ((bytes(7 + bytesOffset) & 0xff) << 24) |
      ((bytes(6 + bytesOffset) & 0xff) << 16) |
      ((bytes(5 + bytesOffset) & 0xff) << 8) | bytes(4 + bytesOffset) & 0xff

    tmpBlockWords(2) = ((bytes(11 + bytesOffset) & 0xff) << 24) |
      ((bytes(10 + bytesOffset) & 0xff) << 16) |
      ((bytes(9 + bytesOffset) & 0xff) << 8) | bytes(8 + bytesOffset) & 0xff

    tmpBlockWords(3) = ((bytes(15 + bytesOffset) & 0xff) << 24) |
      ((bytes(14 + bytesOffset) & 0xff) << 16) |
      ((bytes(13 + bytesOffset) & 0xff) << 8) | bytes(12 + bytesOffset) & 0xff

    tmpBlockWords(4) = ((bytes(19 + bytesOffset) & 0xff) << 24) |
      ((bytes(18 + bytesOffset) & 0xff) << 16) |
      ((bytes(17 + bytesOffset) & 0xff) << 8) | bytes(16 + bytesOffset) & 0xff

    tmpBlockWords(5) = ((bytes(23 + bytesOffset) & 0xff) << 24) |
      ((bytes(22 + bytesOffset) & 0xff) << 16) |
      ((bytes(21 + bytesOffset) & 0xff) << 8) | bytes(20 + bytesOffset) & 0xff

    tmpBlockWords(6) = ((bytes(27 + bytesOffset) & 0xff) << 24) |
      ((bytes(26 + bytesOffset) & 0xff) << 16) |
      ((bytes(25 + bytesOffset) & 0xff) << 8) | bytes(24 + bytesOffset) & 0xff

    tmpBlockWords(7) = ((bytes(31 + bytesOffset) & 0xff) << 24) |
      ((bytes(30 + bytesOffset) & 0xff) << 16) |
      ((bytes(29 + bytesOffset) & 0xff) << 8) | bytes(28 + bytesOffset) & 0xff

    tmpBlockWords(8) = ((bytes(35 + bytesOffset) & 0xff) << 24) |
      ((bytes(34 + bytesOffset) & 0xff) << 16) |
      ((bytes(33 + bytesOffset) & 0xff) << 8) | bytes(32 + bytesOffset) & 0xff

    tmpBlockWords(9) = ((bytes(39 + bytesOffset) & 0xff) << 24) |
      ((bytes(38 + bytesOffset) & 0xff) << 16) |
      ((bytes(37 + bytesOffset) & 0xff) << 8) | bytes(36 + bytesOffset) & 0xff

    tmpBlockWords(10) = ((bytes(43 + bytesOffset) & 0xff) << 24) |
      ((bytes(42 + bytesOffset) & 0xff) << 16) |
      ((bytes(41 + bytesOffset) & 0xff) << 8) | bytes(40 + bytesOffset) & 0xff

    tmpBlockWords(11) = ((bytes(47 + bytesOffset) & 0xff) << 24) |
      ((bytes(46 + bytesOffset) & 0xff) << 16) |
      ((bytes(45 + bytesOffset) & 0xff) << 8) | bytes(44 + bytesOffset) & 0xff

    tmpBlockWords(12) = ((bytes(51 + bytesOffset) & 0xff) << 24) |
      ((bytes(50 + bytesOffset) & 0xff) << 16) |
      ((bytes(49 + bytesOffset) & 0xff) << 8) | bytes(48 + bytesOffset) & 0xff

    tmpBlockWords(13) = ((bytes(55 + bytesOffset) & 0xff) << 24) |
      ((bytes(54 + bytesOffset) & 0xff) << 16) |
      ((bytes(53 + bytesOffset) & 0xff) << 8) | bytes(52 + bytesOffset) & 0xff

    tmpBlockWords(14) = ((bytes(59 + bytesOffset) & 0xff) << 24) |
      ((bytes(58 + bytesOffset) & 0xff) << 16) |
      ((bytes(57 + bytesOffset) & 0xff) << 8) | bytes(56 + bytesOffset) & 0xff

    tmpBlockWords(15) = ((bytes(63 + bytesOffset) & 0xff) << 24) |
      ((bytes(62 + bytesOffset) & 0xff) << 16) |
      ((bytes(61 + bytesOffset) & 0xff) << 8) | bytes(60 + bytesOffset) & 0xff
  }

  private def loopImpl(
      bytes: Array[Byte], bytesOffset: Int, tmpBlockWords: Array[Int]
  ): Unit = {
    var i = 0
    var o = bytesOffset
    while (i < 16) {
      var r = bytes(o) & 0xff
      r |= (bytes(1 + o) & 0xff) << 8
      r |= (bytes(2 + o) & 0xff) << 16
      r |= (bytes(3 + o) & 0xff) << 24
      tmpBlockWords(i) = r
      i += 1
      o += 4
    }
  }

  private def loop2Impl(
      bytes: Array[Byte], bytesOffset: Int, tmpBlockWords: Array[Int]
  ): Unit = {
    var i = 0
    var o = bytesOffset
    while (i < 16) {
      var r = bytes(o) & 0xff
      o += 1
      r |= (bytes(o) & 0xff) << 8
      o += 1
      r |= (bytes(o) & 0xff) << 16
      o += 1
      r |= (bytes(o) & 0xff) << 24
      tmpBlockWords(i) = r
      i += 1
      o += 1
    }
  }
}
