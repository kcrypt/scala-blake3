package ky.korins.blake3

import CommonFunction._

object Blake3 {
  def newHasher(): Hasher =
    new HasherImpl(IV, 0)

  def newKeyedHasher(key: Array[Byte]): Hasher = {
    assert(key.length == KEY_LEN, "key should be ky.korins.blake3.KEY_LEN bytes")
    new HasherImpl(wordsFromLittleEndianBytes(key), KEYED_HASH)
  }

  def newDeriveKeyHasher(context: String): Hasher = {
    val contextKey = new HasherImpl(IV, DERIVE_KEY_CONTEXT)
      .update(context.getBytes)
      .finalize(KEY_LEN)
    val contextKeyWords = first8Words(wordsFromLittleEndianBytes(contextKey))
    new HasherImpl(contextKeyWords, DERIVE_KEY_MATERIAL)
  }

  def hash(source: Array[Byte], len: Int): Array[Byte] =
    newHasher().update(source).finalize(len)

  def hex(source: Array[Byte], resultLength: Int): String = {
    assert(resultLength % 2 == 0, "resultLength should be even")
    newHasher().update(source).finalizeHex(resultLength / 2)
  }

  def bigInt(source: Array[Byte], bitLength: Int): BigInt = {
    assert(bitLength % 8 == 0, "bitLength should be a multiple of 8")
    BigInt(newHasher().update(source).finalize(bitLength / 8))
  }

}
