package ky.korins.blake3

import CommonFunction._

object Blake3 {
  /**
   * A new hasher
   */
  def newHasher(): Hasher =
    new HasherImpl(IV, 0)

  /**
   * A new keyed hasher where key is 32 byte
   */
  def newKeyedHasher(key: Array[Byte]): Hasher = {
    assert(key.length == KEY_LEN, "key should be ky.korins.blake3.KEY_LEN bytes")
    new HasherImpl(wordsFromLittleEndianBytes(key), KEYED_HASH)
  }

  /**
   * A new hasher with derive key that might be any string
   */
  def newDeriveKeyHasher(context: String): Hasher = {
    val contextKey = new HasherImpl(IV, DERIVE_KEY_CONTEXT)
      .update(context.getBytes)
      .done(KEY_LEN)
    val contextKeyWords = first8Words(wordsFromLittleEndianBytes(contextKey))
    new HasherImpl(contextKeyWords, DERIVE_KEY_MATERIAL)
  }

  /**
   * Compute a hash of specified len from specified source
   */
  def hash(source: Array[Byte], len: Int): Array[Byte] =
    newHasher().update(source).done(len)

  /**
   * Compute a hex representative of hash of specified len from specified source
   */
  def hex(source: Array[Byte], resultLength: Int): String = {
    assert(resultLength % 2 == 0, "resultLength should be even")
    newHasher().update(source).doneHex(resultLength / 2)
  }

  /**
   * Compute a BigInt representative of hash of specified len from specified source
   */
  def bigInt(source: Array[Byte], bitLength: Int): BigInt = {
    assert(bitLength % 8 == 0, "bitLength should be a multiple of 8")
    BigInt(newHasher().update(source).done(bitLength / 8))
  }

}
