package ky.korins.blake3

trait Hasher {
  /**
   * Updates a hasher by provided bytes, returns the same hasher
   */
  def update(input: Array[Byte]): Hasher

  /**
   * Calculate a hash into specified byte array
   */
  def done(out_slice: Array[Byte]): Unit

  /**
   * Create a new byte array of specified length and calculate a hash into this array
   */
  def done(len: Int): Array[Byte] = {
    val bytes = new Array[Byte](len)
    done(bytes)
    bytes
  }

  /**
   * Create a hex representative of calculated hash for specified length
   */
  def doneHex(len: Int): String =
    done(len).map(b => "%02x" format (b & 0xff)).mkString
}
