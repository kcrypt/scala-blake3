package ky.korins.blake3

import CommonFunction._

import java.nio.{ByteBuffer, ByteOrder}

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

  def root_output_bytes(out: Array[Byte], offset: Int, len: Int): Unit = synchronized {
    var outputBlockCounter = 0
    val it = (offset until (offset + len)).grouped(2 * OUT_LEN)
    while (it.hasNext) {
      val idxes = it.next()
      val words = compress(
        inputChainingValue,
        blockWords,
        outputBlockCounter,
        blockLen,
        flags | ROOT
      )
      // The output length might not be a multiple of 4.
      val pairs = words.iterator.zip(idxes.grouped(4))
      val bytes = ByteBuffer.allocate(4)
      bytes.order(ByteOrder.LITTLE_ENDIAN)
      while (pairs.hasNext) {
        val (word, idxes) = pairs.next()
        bytes.clear()
        bytes.putInt(word)
        var i = 0
        val jMax = Math.min(idxes.length, bytes.position())
        while (i < jMax) {
          out(idxes(i)) = bytes.get(i)
          i += 1
        }
      }
      outputBlockCounter += 1
    }
  }

  def root_output_byte(): Byte = synchronized {
    compressSingle(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    ).toByte
  }
}
