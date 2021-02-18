/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

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
  @throws(classOf[IllegalArgumentException])
  def newKeyedHasher(key: Array[Byte]): Hasher = {
    if (key.length != KEY_LEN) {
      throw new IllegalArgumentException(
        s"key should be ky.korins.blake3.KEY_LEN: $KEY_LEN bytes"
      )
    }
    new HasherImpl(wordsFromLittleEndianBytes(key), KEYED_HASH)
  }

  /**
   * A new hasher with derive key that should be initalized via callback
   */
  def newDeriveKeyHasher(cb: Hasher => Unit): Hasher = {
    val contextKeyHasher = new HasherImpl(IV, DERIVE_KEY_CONTEXT)
    cb(contextKeyHasher)
    val contextKey = contextKeyHasher.done(KEY_LEN)
    new HasherImpl(wordsFromLittleEndianBytes(contextKey), DERIVE_KEY_MATERIAL)
  }

  /**
   * A new hasher with derive key that might be any array of bytes
   */
  def newDeriveKeyHasher(context: Array[Byte]): Hasher =
    newDeriveKeyHasher(_.update(context))

  /**
   * A new hasher with derive key that might be any string
   */
  def newDeriveKeyHasher(context: String): Hasher =
    newDeriveKeyHasher(context.getBytes)

  /**
   * Compute a hash of specified len from specified source
   */
  def hash(source: Array[Byte], len: Int): Array[Byte] =
    newHasher().update(source).done(len)

  /**
   * Compute a hash of specified len from specified source
   */
  def hash(source: String, len: Int): Array[Byte] =
    hash(source.getBytes, len)

  /**
   * Compute a hash as single byte from specified source
   */
  def hash(source: Array[Byte]): Byte =
    newHasher().update(source).done()

  /**
   * Compute a hash as single byte from specified source
   */
  def hash(source: String): Byte =
    hash(source.getBytes)

  /**
   * Compute a hash as single short from specified source
   */
  def hashShort(source: Array[Byte]): Short =
    newHasher().update(source).doneShort()

  /**
   * Compute a hash as single int from specified source
   */
  def hashShort(source: String): Short =
    hashShort(source.getBytes)

  /**
   * Compute a hash as single int from specified source
   */
  def hashInt(source: Array[Byte]): Int =
    newHasher().update(source).doneInt()

  /**
   * Compute a hash as single int from specified source
   */
  def hashInt(source: String): Int =
    hashInt(source.getBytes)

  /**
   * Compute a hash as single long from specified source
   */
  def hashLong(source: Array[Byte]): Long =
    newHasher().update(source).doneLong()

  /**
   * Compute a hash as single long from specified source
   */
  def hashLong(source: String): Long =
    hashLong(source.getBytes)

  /**
   * Compute a hex representative of hash of specified output len from specified source
   */
  def hex(source: Array[Byte], resultLength: Int): String =
    newHasher().update(source).doneHex(resultLength)

  /**
   * Compute a hex representative of hash of specified output len from specified source
   */
  def hex(source: String, resultLength: Int): String =
    hex(source.getBytes, resultLength)

  /**
   * Compute a BigInt representative of hash of specified len from specified source
   */
  def bigInt(source: Array[Byte], bitLength: Int): BigInt =
    newHasher().update(source).doneBigInt(bitLength)

  /**
   * Compute a BigInt representative of hash of specified len from specified source
   */
  def bigInt(source: String, bitLength: Int): BigInt =
    bigInt(source.getBytes, bitLength)

  /**
   * Compute a BigInt representative of hash and return it as positive BigInt `(mod N)`
   */
  def bigInt(source: Array[Byte], N: BigInt): BigInt =
    newHasher().update(source).doneBigInt(N)

  /**
   * Compute a BigInt representative of hash and return it as positive BigInt `(mod N)`
   */
  def bigInt(source: String, N: BigInt): BigInt =
    bigInt(source.getBytes, N)

  /**
   * Compute a hash of specified len from specified source and returns as base16 encoded string
   */
  def base16(source: Array[Byte], len: Int): String =
    newHasher().update(source).doneBase16(len)

  /**
   * Compute a hash of specified len from specified source and returns as base16 encoded string
   */
  def base16(source: String, len: Int): String =
    base16(source.getBytes, len)

  /**
   * Compute a hash of specified len from specified source and returns as base32 encoded string
   */
  def base32(source: Array[Byte], len: Int): String =
    newHasher().update(source).doneBase32(len)

  /**
   * Compute a hash of specified len from specified source and returns as base32 encoded string
   */
  def base32(source: String, len: Int): String =
    base32(source.getBytes, len)

  /**
   * Compute a hash of specified len from specified source and returns as base32 hex-compatibly encoded string
   */
  def base32Hex(source: Array[Byte], len: Int): String =
    newHasher().update(source).doneBase32Hex(len)

  /**
   * Compute a hash of specified len from specified source and returns as base32 hex-compatibly encoded string
   */
  def base32Hex(source: String, len: Int): String =
    base32Hex(source.getBytes, len)

  /**
   * Compute a hash of specified len from specified source and returns as base64 encoded string
   */
  def base64(source: Array[Byte], len: Int): String =
    newHasher().update(source).doneBase64(len)

  /**
   * Compute a hash of specified len from specified source and returns as base64 encoded string
   */
  def base64(source: String, len: Int): String =
    base64(source.getBytes, len)

  /**
   * Compute a hash of specified len from specified source and returns as base64 URL-safe encoded string
   */
  def base64Url(source: Array[Byte], len: Int): String =
    newHasher().update(source).doneBase64Url(len)

  /**
   * Compute a hash of specified len from specified source and returns as base64 URL-safe encoded string
   */
  def base64Url(source: String, len: Int): String =
    base64Url(source.getBytes, len)
}
