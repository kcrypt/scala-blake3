package ky.korins.blake3

import CommonFunction._

import java.nio.{ByteBuffer, ByteOrder}

private[blake3] class Output (
  val inputChainingValue: Vector[Int],
  val blockWords: Vector[Int],
  val counter: Long,
  val blockLen: Int,
  val flags: Int
) {

  def chainingValue(): Vector[Int] =
    first8Words(compress(
      inputChainingValue,
      blockWords,
      counter,
      blockLen,
      flags
    ))

  def root_output_bytes(out: Array[Byte], offset: Int, len: Int): Unit = {
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
      val pairs = words.zip(idxes.grouped(4).toSeq)
      var i = 0
      val bytes = ByteBuffer.allocate(4)
      bytes.order(ByteOrder.LITTLE_ENDIAN)
      while (i < pairs.length) {
        val (word, idxes) = pairs(i)
        bytes.clear()
        bytes.putInt(word)
        var j = 0
        val jMax = Math.min(idxes.length, bytes.position())
        while (j < jMax) {
          out(idxes(j)) = bytes.get(j)
          j += 1
        }
        i += 1
      }
      outputBlockCounter += 1
    }
  }

  def root_output_byte(): Byte =
    compressSingle(
      inputChainingValue,
      blockWords,
      0,
      blockLen,
      flags | ROOT
    ).toByte
}
