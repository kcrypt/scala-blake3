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

import CommonFunction._

import java.io._
import java.nio.ByteBuffer
import scala.language.implicitConversions

private[blake3] class Output(
  val inputChainingValue: Array[Int],
  val blockWords: Array[Int],
  val counter: Long,
  val blockLen: Int,
  val flags: Int
) {

  def chainingValue(chainingValue: Array[Int]): Unit =
    compressInPlace(
      chainingValue,
      inputChainingValue,
      blockWords,
      counter,
      blockLen,
      flags
    )

  def rootBytes(out: Array[Byte], off: Int, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = off
    val lim = off + len

    val blockLenWords = BLOCK_LEN_WORDS
    val words = new Array[Int](blockLenWords)
    val flags = this.flags | ROOT

    while (pos < lim) {
      compressInPlace(
        words,
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < lim) {
        val word = words(wordIdx)
        wordIdx += 1
        lim - pos match {
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
      }

      outputBlockCounter += 1
    }
  }

  def rootBytesXor(
    in: Array[Byte],
    inOff: Int,
    out: Array[Byte],
    outOff: Int,
    len: Int
  ): Unit = {
    var outputBlockCounter = 0
    var inPos = inOff
    var outPos = outOff
    val outLim = outOff + len

    val blockLenWords = BLOCK_LEN_WORDS
    val words = new Array[Int](blockLenWords)
    val flags = this.flags | ROOT

    while (outPos < outLim) {
      compressInPlace(
        words,
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && outPos < outLim) {
        val word = words(wordIdx)
        wordIdx += 1
        outLim - outPos match {
          case 1 =>
            out(outPos) = (in(inPos) ^ word).toByte
            inPos += 1
            outPos += 1

          case 2 =>
            out(outPos) = (in(inPos) ^ word).toByte
            inPos += 1
            outPos += 1
            out(outPos) = (in(inPos) ^ word >>> 8).toByte
            inPos += 1
            outPos += 1

          case 3 =>
            out(outPos) = (in(inPos) ^ word).toByte
            inPos += 1
            outPos += 1
            out(outPos) = (in(inPos) ^ word >>> 8).toByte
            inPos += 1
            outPos += 1
            out(outPos) = (in(inPos) ^ word >>> 16).toByte
            inPos += 1
            outPos += 1

          case _ =>
            out(outPos) = (in(inPos) ^ word).toByte
            inPos += 1
            outPos += 1
            out(outPos) = (in(inPos) ^ word >>> 8).toByte
            inPos += 1
            outPos += 1
            out(outPos) = (in(inPos) ^ word >>> 16).toByte
            inPos += 1
            outPos += 1
            out(outPos) = (in(inPos) ^ word >>> 24).toByte
            inPos += 1
            outPos += 1
        }
      }

      outputBlockCounter += 1
    }
  }

  def rootByte(): Byte =
    compressSingle(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    ).toByte

  def rootShort(): Short =
    compressSingle(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    ).toShort

  def rootInt(): Int =
    compressSingle(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    )

  def rootLong(): Long =
    compressSingleLong(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    )

  def rootBytes(out: OutputStream, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val blockLenWords = BLOCK_LEN_WORDS
    val words = new Array[Int](blockLenWords)
    val flags = this.flags | ROOT

    while (pos < len) {
      compressInPlace(
        words,
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < len) {
        val word = words(wordIdx)
        wordIdx += 1
        len - pos match {
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
      }

      outputBlockCounter += 1
    }
  }

  def rootBytesXor(in: InputStream, out: OutputStream, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val blockLenWords = BLOCK_LEN_WORDS
    val words = new Array[Int](blockLenWords)
    val flags = this.flags | ROOT

    while (pos < len) {
      compressInPlace(
        words,
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < len) {
        val word = words(wordIdx)
        wordIdx += 1
        len - pos match {
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
      }

      outputBlockCounter += 1
    }
  }

  def rootBytes(out: ByteBuffer, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val blockLenWords = BLOCK_LEN_WORDS
    val words = new Array[Int](blockLenWords)
    val flags = this.flags | ROOT

    while (pos < len) {
      compressInPlace(
        words,
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < len) {
        val word = words(wordIdx)
        wordIdx += 1
        len - pos match {
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
      }

      outputBlockCounter += 1
    }
  }

  def rootBytesXor(in: ByteBuffer, out: ByteBuffer, len: Int): Unit = {
    var outputBlockCounter = 0
    var pos = 0

    val blockLenWords = BLOCK_LEN_WORDS
    val words = new Array[Int](blockLenWords)
    val flags = this.flags | ROOT

    while (pos < len) {
      compressInPlace(
        words,
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags
      )

      var wordIdx = 0
      while (wordIdx < blockLenWords && pos < len) {
        val word = words(wordIdx)
        wordIdx += 1
        len - pos match {
          case 1 =>
            out put (in.get() ^ word).toByte
            pos += 1

          case 2 =>
            out put (in.get() ^ word).toByte
            out put (in.get() ^ word >>> 8).toByte
            pos += 2

          case 3 =>
            out put (in.get() ^ word).toByte
            out put (in.get() ^ word >>> 8).toByte
            out put (in.get() ^ word >>> 16).toByte
            pos += 3

          case _ =>
            out put (in.get() ^ word).toByte
            out put (in.get() ^ word >>> 8).toByte
            out put (in.get() ^ word >>> 16).toByte
            out put (in.get() ^ word >>> 24).toByte
            pos += 4
        }
      }

      outputBlockCounter += 1
    }
  }
}
