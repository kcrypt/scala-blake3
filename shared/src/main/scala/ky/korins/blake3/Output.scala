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
    while (pos < lim) {
      val words = compress(
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags | ROOT
      )

      var wordIdx = 0
      val wordsLimit = Math.min(words.length, lim - pos)
      while (wordIdx < wordsLimit) {
        val word = words(wordIdx)
        wordIdx += 1

        var off = 0
        val outLimit = Math.min(lim, pos + 4)
        while (pos < outLimit) {
          out(pos) = ((word >>> off) & 0xff).toByte
          pos += 1
          off += 8
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
