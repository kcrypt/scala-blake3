package ky.korins.blake3

trait Hasher {
  def update(input: Array[Byte]): Hasher

  def done(out_slice: Array[Byte]): Unit

  def done(len: Int): Array[Byte] = {
    val bytes = new Array[Byte](len)
    done(bytes)
    bytes
  }

  // hex representative of finalized bytes
  def doneHex(len: Int): String =
    done(len).map(b => "%02x" format (b & 0xff)).mkString
}
