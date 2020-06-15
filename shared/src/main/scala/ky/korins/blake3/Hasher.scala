package ky.korins.blake3

trait Hasher {
  def update(input: Array[Byte]): Hasher

  def finalize(out_slice: Array[Byte]): Unit

  def finalize(len: Int): Array[Byte] = {
    val bytes = Array.fill[Byte](len)(0)
    finalize(bytes)
    bytes
  }

  // hex representative of finalized bytes
  def finalizeHex(len: Int): String =
    finalize(len).map(b => "%02x" format (b & 0xff)).mkString
}
