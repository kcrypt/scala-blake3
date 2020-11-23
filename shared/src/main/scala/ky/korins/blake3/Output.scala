package ky.korins.blake3

import CommonFunction._

import java.io.OutputStream
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
            out(pos) = (word & 0xff).toByte
            pos += 1

          case 2 =>
            out(pos) = (word & 0xff).toByte
            out(pos + 1) = ((word >>> 8) & 0xff).toByte
            pos += 2

          case 3 =>
            out(pos) = (word & 0xff).toByte
            out(pos + 1) = ((word >>> 8) & 0xff).toByte
            out(pos + 2) = ((word >>> 16) & 0xff).toByte
            pos += 3

          case _ =>
            out(pos) = (word & 0xff).toByte
            out(pos + 1) = ((word >>> 8) & 0xff).toByte
            out(pos + 2) = ((word >>> 16) & 0xff).toByte
            out(pos + 3) = ((word >>> 24) & 0xff).toByte
            pos += 4
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

  def rootBytes(out: ByteBuffer): Unit = {
    var outputBlockCounter = 0
    var pos = 0
    val len = out.limit()

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
            out.put((word & 0xff).toByte)
            pos += 1

          case 2 =>
            out.put((word & 0xff).toByte)
            out.put(((word >>> 8) & 0xff).toByte)
            pos += 2

          case 3 =>
            out.put((word & 0xff).toByte)
            out.put(((word >>> 8) & 0xff).toByte)
            out.put(((word >>> 16) & 0xff).toByte)
            pos += 3

          case _ =>
            out.put((word & 0xff).toByte)
            out.put(((word >>> 8) & 0xff).toByte)
            out.put(((word >>> 16) & 0xff).toByte)
            out.put(((word >>> 24) & 0xff).toByte)
            pos += 4
        }
      }

      outputBlockCounter += 1
    }
  }
}
