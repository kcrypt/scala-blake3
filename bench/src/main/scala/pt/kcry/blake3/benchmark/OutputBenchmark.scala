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

import CompressRounds._

import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class OutputBenchmark {
  private val bytes = new Array[Byte](CHUNK_LEN)

  private val blockWords = new Array[Int](BLOCK_LEN_WORDS)
  private val tmpBlockWords = new Array[Int](BLOCK_LEN_WORDS)
  private val inputChainingValue = new Array[Int](BLOCK_LEN_WORDS)

  private var blockLen: Int = 0
  private var flags: Int = 0

  @Setup
  def setup(): Unit = {
    val random = new Random()
    for (i <- 0 until BLOCK_LEN_WORDS) {
      blockWords(i) = random.nextInt()
      tmpBlockWords(i) = random.nextInt()
      inputChainingValue(i) = random.nextInt()
    }

    blockLen = random.nextInt()
    flags = random.nextInt()
  }

  @Benchmark
  def hugeChunk(): Unit = hugeChunkImpl(bytes, 0, CHUNK_LEN)

  @Benchmark
  def subLoop(): Unit = subLoopImpl(bytes, 0, CHUNK_LEN)

  @Benchmark
  def subLoopInline(): Unit = subLoopInlineImpl(bytes, 0, CHUNK_LEN)

  private def subLoopImpl(out: Array[Byte], off: Int, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = off
    val lim = off + len

    val blockLenWords = BLOCK_LEN_WORDS
    val words = tmpBlockWords
    val flags = this.flags | ROOT

    while (pos < lim) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < lim) {
        val word = words(wordIdx)
        lim - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; lim: $lim; wordIdx: $wordIdx; off: $off; len: $len"
            )

          case 1 =>
            out(pos) = word.toByte
            pos += 1

          case 2 =>
            out(pos) = word.toByte
            pos += 1
            out(pos) = (word >>> 8).toByte
            pos += 1

          case 3 =>
            out(pos) = word.toByte
            pos += 1
            out(pos) = (word >>> 8).toByte
            pos += 1
            out(pos) = (word >>> 16).toByte
            pos += 1

          case _ =>
            out(pos) = word.toByte
            pos += 1
            out(pos) = (word >>> 8).toByte
            pos += 1
            out(pos) = (word >>> 16).toByte
            pos += 1
            out(pos) = (word >>> 24).toByte
            pos += 1
        }
        wordIdx += 1
      }

      outputBlockCounter += 1
    }
  }

  private def subLoopInlineImpl(out: Array[Byte], off: Int, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = off
    val lim = off + len

    val blockLenWords = BLOCK_LEN_WORDS
    val words = tmpBlockWords
    val flags = this.flags | ROOT

    while (pos < lim) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < lim) {
        val word = words(wordIdx)
        lim - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; lim: $lim; wordIdx: $wordIdx; off: $off; len: $len"
            )

          case 1 =>
            out(pos) = word.toByte
            pos += 1

          case 2 =>
            out(pos) = word.toByte
            out(1 + pos) = (word >>> 8).toByte
            pos += 2

          case 3 =>
            out(pos) = word.toByte
            out(1 + pos) = (word >>> 8).toByte
            out(2 + pos) = (word >>> 16).toByte
            pos += 3

          case _ =>
            out(pos) = word.toByte
            out(1 + pos) = (word >>> 8).toByte
            out(2 + pos) = (word >>> 16).toByte
            out(3 + pos) = (word >>> 24).toByte
            pos += 4
        }
        wordIdx += 1
      }

      outputBlockCounter += 1
    }
  }

  private def hugeChunkImpl(out: Array[Byte], off: Int, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = off

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    var lim = off + len - 63
    while (pos < lim) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      val word_0 = words(0)
      val word_1 = words(1)
      val word_2 = words(2)
      val word_3 = words(3)
      val word_4 = words(4)
      val word_5 = words(5)
      val word_6 = words(6)
      val word_7 = words(7)
      val word_8 = words(8)
      val word_9 = words(9)
      val word_10 = words(10)
      val word_11 = words(11)
      val word_12 = words(12)
      val word_13 = words(13)
      val word_14 = words(14)
      val word_15 = words(15)

      out(pos) = word_0.toByte
      out(1 + pos) = (word_0 >>> 8).toByte
      out(2 + pos) = (word_0 >>> 16).toByte
      out(3 + pos) = (word_0 >>> 24).toByte
      out(4 + pos) = word_1.toByte
      out(5 + pos) = (word_1 >>> 8).toByte
      out(6 + pos) = (word_1 >>> 16).toByte
      out(7 + pos) = (word_1 >>> 24).toByte
      out(8 + pos) = word_2.toByte
      out(9 + pos) = (word_2 >>> 8).toByte
      out(10 + pos) = (word_2 >>> 16).toByte
      out(11 + pos) = (word_2 >>> 24).toByte
      out(12 + pos) = word_3.toByte
      out(13 + pos) = (word_3 >>> 8).toByte
      out(14 + pos) = (word_3 >>> 16).toByte
      out(15 + pos) = (word_3 >>> 24).toByte
      out(16 + pos) = word_4.toByte
      out(17 + pos) = (word_4 >>> 8).toByte
      out(18 + pos) = (word_4 >>> 16).toByte
      out(19 + pos) = (word_4 >>> 24).toByte
      out(20 + pos) = word_5.toByte
      out(21 + pos) = (word_5 >>> 8).toByte
      out(22 + pos) = (word_5 >>> 16).toByte
      out(23 + pos) = (word_5 >>> 24).toByte
      out(24 + pos) = word_6.toByte
      out(25 + pos) = (word_6 >>> 8).toByte
      out(26 + pos) = (word_6 >>> 16).toByte
      out(27 + pos) = (word_6 >>> 24).toByte
      out(28 + pos) = word_7.toByte
      out(29 + pos) = (word_7 >>> 8).toByte
      out(30 + pos) = (word_7 >>> 16).toByte
      out(31 + pos) = (word_7 >>> 24).toByte
      out(32 + pos) = word_8.toByte
      out(33 + pos) = (word_8 >>> 8).toByte
      out(34 + pos) = (word_8 >>> 16).toByte
      out(35 + pos) = (word_8 >>> 24).toByte
      out(36 + pos) = word_9.toByte
      out(37 + pos) = (word_9 >>> 8).toByte
      out(38 + pos) = (word_9 >>> 16).toByte
      out(39 + pos) = (word_9 >>> 24).toByte
      out(40 + pos) = word_10.toByte
      out(41 + pos) = (word_10 >>> 8).toByte
      out(42 + pos) = (word_10 >>> 16).toByte
      out(43 + pos) = (word_10 >>> 24).toByte
      out(44 + pos) = word_11.toByte
      out(45 + pos) = (word_11 >>> 8).toByte
      out(46 + pos) = (word_11 >>> 16).toByte
      out(47 + pos) = (word_11 >>> 24).toByte
      out(48 + pos) = word_12.toByte
      out(49 + pos) = (word_12 >>> 8).toByte
      out(50 + pos) = (word_12 >>> 16).toByte
      out(51 + pos) = (word_12 >>> 24).toByte
      out(52 + pos) = word_13.toByte
      out(53 + pos) = (word_13 >>> 8).toByte
      out(54 + pos) = (word_13 >>> 16).toByte
      out(55 + pos) = (word_13 >>> 24).toByte
      out(56 + pos) = word_14.toByte
      out(57 + pos) = (word_14 >>> 8).toByte
      out(58 + pos) = (word_14 >>> 16).toByte
      out(59 + pos) = (word_14 >>> 24).toByte
      out(60 + pos) = word_15.toByte
      out(61 + pos) = (word_15 >>> 8).toByte
      out(62 + pos) = (word_15 >>> 16).toByte
      out(63 + pos) = (word_15 >>> 24).toByte

      pos += 64
      outputBlockCounter += 1
    }

    lim += 63
    if (pos < lim) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < lim) {
        val word = words(wordIdx)
        lim - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; lim: $lim; wordIdx: $wordIdx; off: $off; len: $len"
            )

          case 1 =>
            out(pos) = word.toByte
            pos += 1

          case 2 =>
            out(pos) = word.toByte
            out(1 + pos) = (word >>> 8).toByte
            pos += 2

          case 3 =>
            out(pos) = word.toByte
            out(1 + pos) = (word >>> 8).toByte
            out(2 + pos) = (word >>> 16).toByte
            pos += 3

          case _ =>
            out(pos) = word.toByte
            out(1 + pos) = (word >>> 8).toByte
            out(2 + pos) = (word >>> 16).toByte
            out(3 + pos) = (word >>> 24).toByte
            pos += 4
        }
        wordIdx += 1
      }
    }
  }
}
