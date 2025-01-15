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

import java.io._
import java.nio.ByteBuffer
import scala.language.implicitConversions

private[blake3] class Output(
    var inputChainingValue: Array[Int], val blockWords: Array[Int],
    var blockLen: Int, var flags: Int, val tmpBlockWords: Array[Int]
) {
  def rootBytes(out: Array[Byte], off: Int, len: Int): Unit = {
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

  def rootBytesXor(
      in: Array[Byte], inOff: Int, out: Array[Byte], outOff: Int, len: Int
  ): Unit = {
    var outputBlockCounter = 0
    var inPos = inOff
    var outPos = outOff

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    var lim = outOff + len - 63
    while (outPos < lim) {
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

      out(outPos) = (in(inPos) ^ word_0).toByte
      out(1 + outPos) = (in(1 + inPos) ^ word_0 >>> 8).toByte
      out(2 + outPos) = (in(2 + inPos) ^ word_0 >>> 16).toByte
      out(3 + outPos) = (in(3 + inPos) ^ word_0 >>> 24).toByte
      out(4 + outPos) = (in(4 + inPos) ^ word_1).toByte
      out(5 + outPos) = (in(5 + inPos) ^ word_1 >>> 8).toByte
      out(6 + outPos) = (in(6 + inPos) ^ word_1 >>> 16).toByte
      out(7 + outPos) = (in(7 + inPos) ^ word_1 >>> 24).toByte
      out(8 + outPos) = (in(8 + inPos) ^ word_2).toByte
      out(9 + outPos) = (in(9 + inPos) ^ word_2 >>> 8).toByte
      out(10 + outPos) = (in(10 + inPos) ^ word_2 >>> 16).toByte
      out(11 + outPos) = (in(11 + inPos) ^ word_2 >>> 24).toByte
      out(12 + outPos) = (in(12 + inPos) ^ word_3).toByte
      out(13 + outPos) = (in(13 + inPos) ^ word_3 >>> 8).toByte
      out(14 + outPos) = (in(14 + inPos) ^ word_3 >>> 16).toByte
      out(15 + outPos) = (in(15 + inPos) ^ word_3 >>> 24).toByte
      out(16 + outPos) = (in(16 + inPos) ^ word_4).toByte
      out(17 + outPos) = (in(17 + inPos) ^ word_4 >>> 8).toByte
      out(18 + outPos) = (in(18 + inPos) ^ word_4 >>> 16).toByte
      out(19 + outPos) = (in(19 + inPos) ^ word_4 >>> 24).toByte
      out(20 + outPos) = (in(20 + inPos) ^ word_5).toByte
      out(21 + outPos) = (in(21 + inPos) ^ word_5 >>> 8).toByte
      out(22 + outPos) = (in(22 + inPos) ^ word_5 >>> 16).toByte
      out(23 + outPos) = (in(23 + inPos) ^ word_5 >>> 24).toByte
      out(24 + outPos) = (in(24 + inPos) ^ word_6).toByte
      out(25 + outPos) = (in(25 + inPos) ^ word_6 >>> 8).toByte
      out(26 + outPos) = (in(26 + inPos) ^ word_6 >>> 16).toByte
      out(27 + outPos) = (in(27 + inPos) ^ word_6 >>> 24).toByte
      out(28 + outPos) = (in(28 + inPos) ^ word_7).toByte
      out(29 + outPos) = (in(29 + inPos) ^ word_7 >>> 8).toByte
      out(30 + outPos) = (in(30 + inPos) ^ word_7 >>> 16).toByte
      out(31 + outPos) = (in(31 + inPos) ^ word_7 >>> 24).toByte
      out(32 + outPos) = (in(32 + inPos) ^ word_8).toByte
      out(33 + outPos) = (in(33 + inPos) ^ word_8 >>> 8).toByte
      out(34 + outPos) = (in(34 + inPos) ^ word_8 >>> 16).toByte
      out(35 + outPos) = (in(35 + inPos) ^ word_8 >>> 24).toByte
      out(36 + outPos) = (in(36 + inPos) ^ word_9).toByte
      out(37 + outPos) = (in(37 + inPos) ^ word_9 >>> 8).toByte
      out(38 + outPos) = (in(38 + inPos) ^ word_9 >>> 16).toByte
      out(39 + outPos) = (in(39 + inPos) ^ word_9 >>> 24).toByte
      out(40 + outPos) = (in(40 + inPos) ^ word_10).toByte
      out(41 + outPos) = (in(41 + inPos) ^ word_10 >>> 8).toByte
      out(42 + outPos) = (in(42 + inPos) ^ word_10 >>> 16).toByte
      out(43 + outPos) = (in(43 + inPos) ^ word_10 >>> 24).toByte
      out(44 + outPos) = (in(44 + inPos) ^ word_11).toByte
      out(45 + outPos) = (in(45 + inPos) ^ word_11 >>> 8).toByte
      out(46 + outPos) = (in(46 + inPos) ^ word_11 >>> 16).toByte
      out(47 + outPos) = (in(47 + inPos) ^ word_11 >>> 24).toByte
      out(48 + outPos) = (in(48 + inPos) ^ word_12).toByte
      out(49 + outPos) = (in(49 + inPos) ^ word_12 >>> 8).toByte
      out(50 + outPos) = (in(50 + inPos) ^ word_12 >>> 16).toByte
      out(51 + outPos) = (in(51 + inPos) ^ word_12 >>> 24).toByte
      out(52 + outPos) = (in(52 + inPos) ^ word_13).toByte
      out(53 + outPos) = (in(53 + inPos) ^ word_13 >>> 8).toByte
      out(54 + outPos) = (in(54 + inPos) ^ word_13 >>> 16).toByte
      out(55 + outPos) = (in(55 + inPos) ^ word_13 >>> 24).toByte
      out(56 + outPos) = (in(56 + inPos) ^ word_14).toByte
      out(57 + outPos) = (in(57 + inPos) ^ word_14 >>> 8).toByte
      out(58 + outPos) = (in(58 + inPos) ^ word_14 >>> 16).toByte
      out(59 + outPos) = (in(59 + inPos) ^ word_14 >>> 24).toByte
      out(60 + outPos) = (in(60 + inPos) ^ word_15).toByte
      out(61 + outPos) = (in(61 + inPos) ^ word_15 >>> 8).toByte
      out(62 + outPos) = (in(62 + inPos) ^ word_15 >>> 16).toByte
      out(63 + outPos) = (in(63 + inPos) ^ word_15 >>> 24).toByte

      inPos += 64
      outPos += 64
      outputBlockCounter += 1
    }

    lim += 63
    if (outPos < lim) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (outPos < lim) {
        val word = words(wordIdx)
        lim - outPos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; outPos: $outPos; lim: $lim; wordIdx: $wordIdx; outOff: $outOff; len: $len"
            )

          case 1 =>
            out(outPos) = (in(inPos) ^ word).toByte
            inPos += 1
            outPos += 1

          case 2 =>
            out(outPos) = (in(inPos) ^ word).toByte
            out(1 + outPos) = (in(1 + inPos) ^ word >>> 8).toByte
            inPos += 2
            outPos += 2

          case 3 =>
            out(outPos) = (in(inPos) ^ word).toByte
            out(1 + outPos) = (in(1 + inPos) ^ word >>> 8).toByte
            out(2 + outPos) = (in(2 + inPos) ^ word >>> 16).toByte
            inPos += 3
            outPos += 3

          case _ =>
            out(outPos) = (in(inPos) ^ word).toByte
            out(1 + outPos) = (in(1 + inPos) ^ word >>> 8).toByte
            out(2 + outPos) = (in(2 + inPos) ^ word >>> 16).toByte
            out(3 + outPos) = (in(3 + inPos) ^ word >>> 24).toByte
            inPos += 4
            outPos += 4
        }
        wordIdx += 1
      }
    }
  }

  def rootByte(): Byte = (compressRounds(
    blockWords, inputChainingValue, 0, blockLen, flags | ROOT
  ) >> 32).toByte

  def rootShort(): Short = (compressRounds(
    blockWords, inputChainingValue, 0, blockLen, flags | ROOT
  ) >> 32).toShort

  def rootInt(): Int = (compressRounds(
    blockWords, inputChainingValue, 0, blockLen, flags | ROOT
  ) >> 32).toInt

  def rootLong(): Long =
    compressRounds(blockWords, inputChainingValue, 0, blockLen, flags | ROOT)

  def rootBytes(out: OutputStream, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    val lim = len - 63
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

      out.write(word_0)
      out.write(word_0 >>> 8)
      out.write(word_0 >>> 16)
      out.write(word_0 >>> 24)
      out.write(word_1)
      out.write(word_1 >>> 8)
      out.write(word_1 >>> 16)
      out.write(word_1 >>> 24)
      out.write(word_2)
      out.write(word_2 >>> 8)
      out.write(word_2 >>> 16)
      out.write(word_2 >>> 24)
      out.write(word_3)
      out.write(word_3 >>> 8)
      out.write(word_3 >>> 16)
      out.write(word_3 >>> 24)
      out.write(word_4)
      out.write(word_4 >>> 8)
      out.write(word_4 >>> 16)
      out.write(word_4 >>> 24)
      out.write(word_5)
      out.write(word_5 >>> 8)
      out.write(word_5 >>> 16)
      out.write(word_5 >>> 24)
      out.write(word_6)
      out.write(word_6 >>> 8)
      out.write(word_6 >>> 16)
      out.write(word_6 >>> 24)
      out.write(word_7)
      out.write(word_7 >>> 8)
      out.write(word_7 >>> 16)
      out.write(word_7 >>> 24)
      out.write(word_8)
      out.write(word_8 >>> 8)
      out.write(word_8 >>> 16)
      out.write(word_8 >>> 24)
      out.write(word_9)
      out.write(word_9 >>> 8)
      out.write(word_9 >>> 16)
      out.write(word_9 >>> 24)
      out.write(word_10)
      out.write(word_10 >>> 8)
      out.write(word_10 >>> 16)
      out.write(word_10 >>> 24)
      out.write(word_11)
      out.write(word_11 >>> 8)
      out.write(word_11 >>> 16)
      out.write(word_11 >>> 24)
      out.write(word_12)
      out.write(word_12 >>> 8)
      out.write(word_12 >>> 16)
      out.write(word_12 >>> 24)
      out.write(word_13)
      out.write(word_13 >>> 8)
      out.write(word_13 >>> 16)
      out.write(word_13 >>> 24)
      out.write(word_14)
      out.write(word_14 >>> 8)
      out.write(word_14 >>> 16)
      out.write(word_14 >>> 24)
      out.write(word_15)
      out.write(word_15 >>> 8)
      out.write(word_15 >>> 16)
      out.write(word_15 >>> 24)

      pos += 64
      outputBlockCounter += 1
    }

    if (pos < len) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < len) {
        val word = words(wordIdx)
        len - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; wordIdx: $wordIdx; len: $len"
            )

          case 1 =>
            out.write(word)
            pos += 1

          case 2 =>
            out.write(word)
            out.write(word >>> 8)
            pos += 2

          case 3 =>
            out.write(word)
            out.write(word >>> 8)
            out.write(word >>> 16)
            pos += 3

          case _ =>
            out.write(word)
            out.write(word >>> 8)
            out.write(word >>> 16)
            out.write(word >>> 24)
            pos += 4
        }
        wordIdx += 1
      }
    }
  }

  def rootBytesXor(in: InputStream, out: OutputStream, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    val lim = len - 63
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

      out.write(in.read() ^ word_0)
      out.write(in.read() ^ word_0 >>> 8)
      out.write(in.read() ^ word_0 >>> 16)
      out.write(in.read() ^ word_0 >>> 24)
      out.write(in.read() ^ word_1)
      out.write(in.read() ^ word_1 >>> 8)
      out.write(in.read() ^ word_1 >>> 16)
      out.write(in.read() ^ word_1 >>> 24)
      out.write(in.read() ^ word_2)
      out.write(in.read() ^ word_2 >>> 8)
      out.write(in.read() ^ word_2 >>> 16)
      out.write(in.read() ^ word_2 >>> 24)
      out.write(in.read() ^ word_3)
      out.write(in.read() ^ word_3 >>> 8)
      out.write(in.read() ^ word_3 >>> 16)
      out.write(in.read() ^ word_3 >>> 24)
      out.write(in.read() ^ word_4)
      out.write(in.read() ^ word_4 >>> 8)
      out.write(in.read() ^ word_4 >>> 16)
      out.write(in.read() ^ word_4 >>> 24)
      out.write(in.read() ^ word_5)
      out.write(in.read() ^ word_5 >>> 8)
      out.write(in.read() ^ word_5 >>> 16)
      out.write(in.read() ^ word_5 >>> 24)
      out.write(in.read() ^ word_6)
      out.write(in.read() ^ word_6 >>> 8)
      out.write(in.read() ^ word_6 >>> 16)
      out.write(in.read() ^ word_6 >>> 24)
      out.write(in.read() ^ word_7)
      out.write(in.read() ^ word_7 >>> 8)
      out.write(in.read() ^ word_7 >>> 16)
      out.write(in.read() ^ word_7 >>> 24)
      out.write(in.read() ^ word_8)
      out.write(in.read() ^ word_8 >>> 8)
      out.write(in.read() ^ word_8 >>> 16)
      out.write(in.read() ^ word_8 >>> 24)
      out.write(in.read() ^ word_9)
      out.write(in.read() ^ word_9 >>> 8)
      out.write(in.read() ^ word_9 >>> 16)
      out.write(in.read() ^ word_9 >>> 24)
      out.write(in.read() ^ word_10)
      out.write(in.read() ^ word_10 >>> 8)
      out.write(in.read() ^ word_10 >>> 16)
      out.write(in.read() ^ word_10 >>> 24)
      out.write(in.read() ^ word_11)
      out.write(in.read() ^ word_11 >>> 8)
      out.write(in.read() ^ word_11 >>> 16)
      out.write(in.read() ^ word_11 >>> 24)
      out.write(in.read() ^ word_12)
      out.write(in.read() ^ word_12 >>> 8)
      out.write(in.read() ^ word_12 >>> 16)
      out.write(in.read() ^ word_12 >>> 24)
      out.write(in.read() ^ word_13)
      out.write(in.read() ^ word_13 >>> 8)
      out.write(in.read() ^ word_13 >>> 16)
      out.write(in.read() ^ word_13 >>> 24)
      out.write(in.read() ^ word_14)
      out.write(in.read() ^ word_14 >>> 8)
      out.write(in.read() ^ word_14 >>> 16)
      out.write(in.read() ^ word_14 >>> 24)
      out.write(in.read() ^ word_15)
      out.write(in.read() ^ word_15 >>> 8)
      out.write(in.read() ^ word_15 >>> 16)
      out.write(in.read() ^ word_15 >>> 24)

      pos += 64
      outputBlockCounter += 1
    }

    if (pos < len) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < len) {
        val word = words(wordIdx)
        len - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; wordIdx: $wordIdx; len: $len"
            )

          case 1 =>
            out.write(in.read() ^ word)
            pos += 1

          case 2 =>
            out.write(in.read() ^ word)
            out.write(in.read() ^ (word >>> 8))
            pos += 2

          case 3 =>
            out.write(in.read() ^ word)
            out.write(in.read() ^ (word >>> 8))
            out.write(in.read() ^ (word >>> 16))
            pos += 3

          case _ =>
            out.write(in.read() ^ word)
            out.write(in.read() ^ (word >>> 8))
            out.write(in.read() ^ (word >>> 16))
            out.write(in.read() ^ (word >>> 24))
            pos += 4
        }
        wordIdx += 1
      }

      outputBlockCounter += 1
    }
  }

  def rootBytes(out: ByteBuffer, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    val lim = len - 63
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

      out put word_0.toByte
      out put (word_0 >>> 8).toByte
      out put (word_0 >>> 16).toByte
      out put (word_0 >>> 24).toByte
      out put word_1.toByte
      out put (word_1 >>> 8).toByte
      out put (word_1 >>> 16).toByte
      out put (word_1 >>> 24).toByte
      out put word_2.toByte
      out put (word_2 >>> 8).toByte
      out put (word_2 >>> 16).toByte
      out put (word_2 >>> 24).toByte
      out put word_3.toByte
      out put (word_3 >>> 8).toByte
      out put (word_3 >>> 16).toByte
      out put (word_3 >>> 24).toByte
      out put word_4.toByte
      out put (word_4 >>> 8).toByte
      out put (word_4 >>> 16).toByte
      out put (word_4 >>> 24).toByte
      out put word_5.toByte
      out put (word_5 >>> 8).toByte
      out put (word_5 >>> 16).toByte
      out put (word_5 >>> 24).toByte
      out put word_6.toByte
      out put (word_6 >>> 8).toByte
      out put (word_6 >>> 16).toByte
      out put (word_6 >>> 24).toByte
      out put word_7.toByte
      out put (word_7 >>> 8).toByte
      out put (word_7 >>> 16).toByte
      out put (word_7 >>> 24).toByte
      out put word_8.toByte
      out put (word_8 >>> 8).toByte
      out put (word_8 >>> 16).toByte
      out put (word_8 >>> 24).toByte
      out put word_9.toByte
      out put (word_9 >>> 8).toByte
      out put (word_9 >>> 16).toByte
      out put (word_9 >>> 24).toByte
      out put word_10.toByte
      out put (word_10 >>> 8).toByte
      out put (word_10 >>> 16).toByte
      out put (word_10 >>> 24).toByte
      out put word_11.toByte
      out put (word_11 >>> 8).toByte
      out put (word_11 >>> 16).toByte
      out put (word_11 >>> 24).toByte
      out put word_12.toByte
      out put (word_12 >>> 8).toByte
      out put (word_12 >>> 16).toByte
      out put (word_12 >>> 24).toByte
      out put word_13.toByte
      out put (word_13 >>> 8).toByte
      out put (word_13 >>> 16).toByte
      out put (word_13 >>> 24).toByte
      out put word_14.toByte
      out put (word_14 >>> 8).toByte
      out put (word_14 >>> 16).toByte
      out put (word_14 >>> 24).toByte
      out put word_15.toByte
      out put (word_15 >>> 8).toByte
      out put (word_15 >>> 16).toByte
      out put (word_15 >>> 24).toByte

      pos += 64
      outputBlockCounter += 1
    }

    if (pos < len) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < len) {
        val word = words(wordIdx)
        len - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; wordIdx: $wordIdx; len: $len"
            )

          case 1 =>
            out put word.toByte
            pos += 1

          case 2 =>
            out put word.toByte
            out put (word >>> 8).toByte
            pos += 2

          case 3 =>
            out put word.toByte
            out put (word >>> 8).toByte
            out put (word >>> 16).toByte
            pos += 3

          case _ =>
            out put word.toByte
            out put (word >>> 8).toByte
            out put (word >>> 16).toByte
            out put (word >>> 24).toByte
            pos += 4
        }
        wordIdx += 1
      }
    }
  }

  def rootBytesXor(in: ByteBuffer, out: ByteBuffer, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    val lim = len - 63
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

      out put (in.get() ^ word_0).toByte
      out put (in.get() ^ (word_0 >>> 8)).toByte
      out put (in.get() ^ (word_0 >>> 16)).toByte
      out put (in.get() ^ (word_0 >>> 24)).toByte
      out put (in.get() ^ word_1).toByte
      out put (in.get() ^ (word_1 >>> 8)).toByte
      out put (in.get() ^ (word_1 >>> 16)).toByte
      out put (in.get() ^ (word_1 >>> 24)).toByte
      out put (in.get() ^ word_2).toByte
      out put (in.get() ^ (word_2 >>> 8)).toByte
      out put (in.get() ^ (word_2 >>> 16)).toByte
      out put (in.get() ^ (word_2 >>> 24)).toByte
      out put (in.get() ^ word_3).toByte
      out put (in.get() ^ (word_3 >>> 8)).toByte
      out put (in.get() ^ (word_3 >>> 16)).toByte
      out put (in.get() ^ (word_3 >>> 24)).toByte
      out put (in.get() ^ word_4).toByte
      out put (in.get() ^ (word_4 >>> 8)).toByte
      out put (in.get() ^ (word_4 >>> 16)).toByte
      out put (in.get() ^ (word_4 >>> 24)).toByte
      out put (in.get() ^ word_5).toByte
      out put (in.get() ^ (word_5 >>> 8)).toByte
      out put (in.get() ^ (word_5 >>> 16)).toByte
      out put (in.get() ^ (word_5 >>> 24)).toByte
      out put (in.get() ^ word_6).toByte
      out put (in.get() ^ (word_6 >>> 8)).toByte
      out put (in.get() ^ (word_6 >>> 16)).toByte
      out put (in.get() ^ (word_6 >>> 24)).toByte
      out put (in.get() ^ word_7).toByte
      out put (in.get() ^ (word_7 >>> 8)).toByte
      out put (in.get() ^ (word_7 >>> 16)).toByte
      out put (in.get() ^ (word_7 >>> 24)).toByte
      out put (in.get() ^ word_8).toByte
      out put (in.get() ^ (word_8 >>> 8)).toByte
      out put (in.get() ^ (word_8 >>> 16)).toByte
      out put (in.get() ^ (word_8 >>> 24)).toByte
      out put (in.get() ^ word_9).toByte
      out put (in.get() ^ (word_9 >>> 8)).toByte
      out put (in.get() ^ (word_9 >>> 16)).toByte
      out put (in.get() ^ (word_9 >>> 24)).toByte
      out put (in.get() ^ word_10).toByte
      out put (in.get() ^ (word_10 >>> 8)).toByte
      out put (in.get() ^ (word_10 >>> 16)).toByte
      out put (in.get() ^ (word_10 >>> 24)).toByte
      out put (in.get() ^ word_11).toByte
      out put (in.get() ^ (word_11 >>> 8)).toByte
      out put (in.get() ^ (word_11 >>> 16)).toByte
      out put (in.get() ^ (word_11 >>> 24)).toByte
      out put (in.get() ^ word_12).toByte
      out put (in.get() ^ (word_12 >>> 8)).toByte
      out put (in.get() ^ (word_12 >>> 16)).toByte
      out put (in.get() ^ (word_12 >>> 24)).toByte
      out put (in.get() ^ word_13).toByte
      out put (in.get() ^ (word_13 >>> 8)).toByte
      out put (in.get() ^ (word_13 >>> 16)).toByte
      out put (in.get() ^ (word_13 >>> 24)).toByte
      out put (in.get() ^ word_14).toByte
      out put (in.get() ^ (word_14 >>> 8)).toByte
      out put (in.get() ^ (word_14 >>> 16)).toByte
      out put (in.get() ^ (word_14 >>> 24)).toByte
      out put (in.get() ^ word_15).toByte
      out put (in.get() ^ (word_15 >>> 8)).toByte
      out put (in.get() ^ (word_15 >>> 16)).toByte
      out put (in.get() ^ (word_15 >>> 24)).toByte

      pos += 64
      outputBlockCounter += 1
    }

    if (pos < len) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < len) {
        val word = words(wordIdx)
        len - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; wordIdx: $wordIdx; len: $len"
            )

          case 1 =>
            out put (in.get() ^ word).toByte
            pos += 1

          case 2 =>
            out put (in.get() ^ word).toByte
            out put (in.get() ^ (word >>> 8)).toByte
            pos += 2

          case 3 =>
            out put (in.get() ^ word).toByte
            out put (in.get() ^ (word >>> 8)).toByte
            out put (in.get() ^ (word >>> 16)).toByte
            pos += 3

          case _ =>
            out put (in.get() ^ word).toByte
            out put (in.get() ^ (word >>> 8)).toByte
            out put (in.get() ^ (word >>> 16)).toByte
            out put (in.get() ^ (word >>> 24)).toByte
            pos += 4
        }
        wordIdx += 1
      }
    }
  }

  def rootBytes[T](out: Byte => T, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    val lim = len - 63
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

      out(word_0.toByte)
      out((word_0 >>> 8).toByte)
      out((word_0 >>> 16).toByte)
      out((word_0 >>> 24).toByte)
      out(word_1.toByte)
      out((word_1 >>> 8).toByte)
      out((word_1 >>> 16).toByte)
      out((word_1 >>> 24).toByte)
      out(word_2.toByte)
      out((word_2 >>> 8).toByte)
      out((word_2 >>> 16).toByte)
      out((word_2 >>> 24).toByte)
      out(word_3.toByte)
      out((word_3 >>> 8).toByte)
      out((word_3 >>> 16).toByte)
      out((word_3 >>> 24).toByte)
      out(word_4.toByte)
      out((word_4 >>> 8).toByte)
      out((word_4 >>> 16).toByte)
      out((word_4 >>> 24).toByte)
      out(word_5.toByte)
      out((word_5 >>> 8).toByte)
      out((word_5 >>> 16).toByte)
      out((word_5 >>> 24).toByte)
      out(word_6.toByte)
      out((word_6 >>> 8).toByte)
      out((word_6 >>> 16).toByte)
      out((word_6 >>> 24).toByte)
      out(word_7.toByte)
      out((word_7 >>> 8).toByte)
      out((word_7 >>> 16).toByte)
      out((word_7 >>> 24).toByte)
      out(word_8.toByte)
      out((word_8 >>> 8).toByte)
      out((word_8 >>> 16).toByte)
      out((word_8 >>> 24).toByte)
      out(word_9.toByte)
      out((word_9 >>> 8).toByte)
      out((word_9 >>> 16).toByte)
      out((word_9 >>> 24).toByte)
      out(word_10.toByte)
      out((word_10 >>> 8).toByte)
      out((word_10 >>> 16).toByte)
      out((word_10 >>> 24).toByte)
      out(word_11.toByte)
      out((word_11 >>> 8).toByte)
      out((word_11 >>> 16).toByte)
      out((word_11 >>> 24).toByte)
      out(word_12.toByte)
      out((word_12 >>> 8).toByte)
      out((word_12 >>> 16).toByte)
      out((word_12 >>> 24).toByte)
      out(word_13.toByte)
      out((word_13 >>> 8).toByte)
      out((word_13 >>> 16).toByte)
      out((word_13 >>> 24).toByte)
      out(word_14.toByte)
      out((word_14 >>> 8).toByte)
      out((word_14 >>> 16).toByte)
      out((word_14 >>> 24).toByte)
      out(word_15.toByte)
      out((word_15 >>> 8).toByte)
      out((word_15 >>> 16).toByte)
      out((word_15 >>> 24).toByte)

      pos += 64
      outputBlockCounter += 1
    }

    if (pos < len) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < len) {
        val word = words(wordIdx)
        len - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; wordIdx: $wordIdx; len: $len"
            )

          case 1 =>
            out(word.toByte)
            pos += 1

          case 2 =>
            out(word.toByte)
            out((word >>> 8).toByte)
            pos += 2

          case 3 =>
            out(word.toByte)
            out((word >>> 8).toByte)
            out((word >>> 16).toByte)
            pos += 3

          case _ =>
            out(word.toByte)
            out((word >>> 8).toByte)
            out((word >>> 16).toByte)
            out((word >>> 24).toByte)
            pos += 4
        }
        wordIdx += 1
      }
    }
  }

  def rootBytesXor[T](in: () => Byte, out: Byte => T, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val words = tmpBlockWords
    val flags = this.flags | ROOT

    val lim = len - 63
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

      out((in() ^ word_0).toByte)
      out((in() ^ (word_0 >>> 8)).toByte)
      out((in() ^ (word_0 >>> 16)).toByte)
      out((in() ^ (word_0 >>> 24)).toByte)
      out((in() ^ word_1).toByte)
      out((in() ^ (word_1 >>> 8)).toByte)
      out((in() ^ (word_1 >>> 16)).toByte)
      out((in() ^ (word_1 >>> 24)).toByte)
      out((in() ^ word_2).toByte)
      out((in() ^ (word_2 >>> 8)).toByte)
      out((in() ^ (word_2 >>> 16)).toByte)
      out((in() ^ (word_2 >>> 24)).toByte)
      out((in() ^ word_3).toByte)
      out((in() ^ (word_3 >>> 8)).toByte)
      out((in() ^ (word_3 >>> 16)).toByte)
      out((in() ^ (word_3 >>> 24)).toByte)
      out((in() ^ word_4).toByte)
      out((in() ^ (word_4 >>> 8)).toByte)
      out((in() ^ (word_4 >>> 16)).toByte)
      out((in() ^ (word_4 >>> 24)).toByte)
      out((in() ^ word_5).toByte)
      out((in() ^ (word_5 >>> 8)).toByte)
      out((in() ^ (word_5 >>> 16)).toByte)
      out((in() ^ (word_5 >>> 24)).toByte)
      out((in() ^ word_6).toByte)
      out((in() ^ (word_6 >>> 8)).toByte)
      out((in() ^ (word_6 >>> 16)).toByte)
      out((in() ^ (word_6 >>> 24)).toByte)
      out((in() ^ word_7).toByte)
      out((in() ^ (word_7 >>> 8)).toByte)
      out((in() ^ (word_7 >>> 16)).toByte)
      out((in() ^ (word_7 >>> 24)).toByte)
      out((in() ^ word_8).toByte)
      out((in() ^ (word_8 >>> 8)).toByte)
      out((in() ^ (word_8 >>> 16)).toByte)
      out((in() ^ (word_8 >>> 24)).toByte)
      out((in() ^ word_9).toByte)
      out((in() ^ (word_9 >>> 8)).toByte)
      out((in() ^ (word_9 >>> 16)).toByte)
      out((in() ^ (word_9 >>> 24)).toByte)
      out((in() ^ word_10).toByte)
      out((in() ^ (word_10 >>> 8)).toByte)
      out((in() ^ (word_10 >>> 16)).toByte)
      out((in() ^ (word_10 >>> 24)).toByte)
      out((in() ^ word_11).toByte)
      out((in() ^ (word_11 >>> 8)).toByte)
      out((in() ^ (word_11 >>> 16)).toByte)
      out((in() ^ (word_11 >>> 24)).toByte)
      out((in() ^ word_12).toByte)
      out((in() ^ (word_12 >>> 8)).toByte)
      out((in() ^ (word_12 >>> 16)).toByte)
      out((in() ^ (word_12 >>> 24)).toByte)
      out((in() ^ word_13).toByte)
      out((in() ^ (word_13 >>> 8)).toByte)
      out((in() ^ (word_13 >>> 16)).toByte)
      out((in() ^ (word_13 >>> 24)).toByte)
      out((in() ^ word_14).toByte)
      out((in() ^ (word_14 >>> 8)).toByte)
      out((in() ^ (word_14 >>> 16)).toByte)
      out((in() ^ (word_14 >>> 24)).toByte)
      out((in() ^ word_15).toByte)
      out((in() ^ (word_15 >>> 8)).toByte)
      out((in() ^ (word_15 >>> 16)).toByte)
      out((in() ^ (word_15 >>> 24)).toByte)

      pos += 64
      outputBlockCounter += 1
    }

    if (pos < len) {
      compressRounds(
        words, blockWords, inputChainingValue, outputBlockCounter, blockLen,
        flags
      )

      var wordIdx = 0
      while (pos < len) {
        val word = words(wordIdx)
        len - pos match {
          case x if x <= 0 =>
            throw new RuntimeException(
              s"x: $x; pos: $pos; wordIdx: $wordIdx; len: $len"
            )

          case 1 =>
            out((in() ^ word).toByte)
            pos += 1

          case 2 =>
            out((in() ^ word).toByte)
            out((in() ^ (word >>> 8)).toByte)
            pos += 2

          case 3 =>
            out((in() ^ word).toByte)
            out((in() ^ (word >>> 8)).toByte)
            out((in() ^ (word >>> 16)).toByte)
            pos += 3

          case _ =>
            out((in() ^ word).toByte)
            out((in() ^ (word >>> 8)).toByte)
            out((in() ^ (word >>> 16)).toByte)
            out((in() ^ (word >>> 24)).toByte)
            pos += 4
        }
        wordIdx += 1
      }
    }
  }
}
