/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it islicensed under the Apache License 2.0.
 */

package ky.korins.blake3

import java.io.{InputStream, OutputStream}
import java.nio.ByteBuffer

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
   * Updates a hasher from specified InputStream, returns the same hasher
   *
   * It reads `input` until it returns `-1`
   */
  def update(input: InputStream): Hasher

  /**
   * Updates a hasher from specified ByteBuffer, returns the same hasher
   */
  def update(input: ByteBuffer): Hasher

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
   * Calculate a hash as single byte
   */
  def done(): Byte

  /**
   * Calculate a hash into specified OutputStream with specified output length in bytes
   */
  def done(out: OutputStream, len: Int): Unit

  /**
   * Calculate a hash into specified OutputStream with specified output length in bytes
   */
  def done(out: ByteBuffer): Unit

  /**
   * Calculate a hash as single short
   */
  def doneShort(): Short

  /**
   * Calculate a hash as single int
   */
  def doneInt(): Int

  /**
   * Calculate a hash as single long
   */
  def doneLong(): Long

  /**
   * Calculate a hash and return it as positive BigInt with specified length in bits
   */
  @throws(classOf[IllegalArgumentException])
  def doneBigInt(bitLength: Int): BigInt = {
    if (bitLength % 8 != 0) {
      throw new IllegalArgumentException(
        s"bitLength: $bitLength should be a multiple of 8"
      )
    }
    BigInt(1, done(bitLength / 8))
  }

  /**
   * Calculate a hash and return it as positive BigInt `(mod N)`
   */
  def doneBigInt(N: BigInt): BigInt = {
    val bytes = N.bitLength match {
      case bitLength if (bitLength % 8 == 0) =>
        bitLength / 8
      case bitLength =>
        bitLength / 8 + 1
    }
    BigInt(1, done(bytes)) mod N
  }

  /**
   * Calculate a hash and return as hex encoded string with specified output length in characters
   */
  @throws(classOf[IllegalArgumentException])
  def doneHex(resultLength: Int): String = {
    if (resultLength % 2 != 0) {
      throw new IllegalArgumentException(
        s"resultLength: $resultLength should be even"
      )
    }
    RFC4648.base16(done(resultLength / 2)).toLowerCase
  }

  /**
   * Create a base16 representative of calculated hash for specified length
   */
  def doneBase16(len: Int): String =
    RFC4648.base16(done(len))

  /**
   * Create a base32 representative of calculated hash for specified length
   */
  def doneBase32(len: Int): String =
    RFC4648.base32(done(len))

  /**
   * Create a base32 hex-compatibly representative of calculated hash for specified length
   */
  def doneBase32Hex(len: Int): String =
    RFC4648.base32_hex(done(len))

  /**
   * Create a base64 representative of calculated hash for specified length
   */
  def doneBase64(len: Int): String =
    RFC4648.base64(done(len))

  /**
   * Create a base64 URL-safe representative of calculated hash for specified length
   */
  def doneBase64Url(len: Int): String =
    RFC4648.base64_url(done(len))
}
