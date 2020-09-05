package ky.korins.blake3

trait Hasher {
  /**
   * Updates a hasher by provided bytes, returns the same hasher
   */
  def update(input: Array[Byte]): Hasher

  /**
   * Updates a hasher by specified part of provided bytes, returns the same hasher
   */
  def update(input: Array[Byte], offset: Int, len: Int): Hasher

  /**
   * Updates a hasher by specified byte, returns the same hasher
   */
  def update(input: Byte): Hasher

  /**
   * Updates a hasher by specified string, returns the same hasher
   */
  def update(input: String): Hasher

  /**
   * Calculate a hash into specified byte array
   */
  def done(out: Array[Byte]): Unit

  /**
   * Calculate a hash into specified part of array
   */
  def done(out: Array[Byte], offset: Int, len: Int): Unit

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

  /**
   * Calculate a hash as single byte
   */
  def done(): Byte

}
