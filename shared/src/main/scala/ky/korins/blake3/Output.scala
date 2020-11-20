package ky.korins.blake3

import CommonFunction._

import scala.language.implicitConversions

private[blake3] class Output (
  val inputChainingValue: Array[Int],
  val blockWords: Array[Int],
  val counter: Long,
  val blockLen: Int,
  val flags: Int
) {

  def chainingValue(): Array[Int] =
    compress(
      inputChainingValue,
      blockWords,
      counter,
      blockLen,
      flags
    )

  def rootBytes(out: Array[Byte], off: Int, len: Int): Unit = synchronized {
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

  def rootByte(): Byte = synchronized {
    compressSingle(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    ).toByte
  }
}
