/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

package ky.korins.blake3

/**
 * Implementing RFC 4648 encoding without padding
 */
object RFC4648 {

  /**
   * RFC 4648 Section 4
   */
  val base64_alphabet: Array[Char] =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray

  /**
   * RFC 4648 Section 5
   */
  val base64_url_alphabet: Array[Char] =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray

  /**
   * RFC 4648 Section 6
   */
  val base32_alphabet: Array[Char] =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray

  /**
   * RFC 4648 Section 7
   */
  val base32_hex_alphabet: Array[Char] =
    "0123456789ABCDEFGHIJKLMNOPQRSTUV".toCharArray

  /**
   * RFC 4648 Section 8
   */
  val base16_alphabet: Array[Char] =
    "0123456789ABCDEF".toCharArray

  def base64_b2c(
    bytes: Array[Byte],
    offset: Int,
    len: Int,
    alphabet: Array[Char],
    sb: StringBuilder
  ): Unit =
    len match {
      case 0 =>
      // do nothing

      case 1 =>
        sb append alphabet((bytes(offset) >>> 2) & 0x3f)
        sb append alphabet((bytes(offset) & 0x3) << 4)

      case 2 =>
        sb append alphabet((bytes(offset) >>> 2) & 0x3f)
        sb append alphabet(
          ((bytes(offset) & 0x3) << 4) | ((bytes(offset + 1) >>> 4) & 0xf)
        )
        sb append alphabet((bytes(offset + 1) & 0xf) << 2)

      case _ => // 3 or more
        sb append alphabet((bytes(offset) >>> 2) & 0x3f)
        sb append alphabet(
          ((bytes(offset) & 0x3) << 4) | ((bytes(offset + 1) >>> 4) & 0xf)
        )
        sb append alphabet(
          ((bytes(offset + 1) & 0xf) << 2) | ((bytes(offset + 2) >>> 6) & 0x3)
        )
        sb append alphabet(bytes(offset + 2) & 0x3f)
    }

  private def base32_b2c(
    bytes: Array[Byte],
    offset: Int,
    len: Int,
    alphabet: Array[Char],
    sb: StringBuilder
  ): Unit =
    len match {
      case 0 =>
      // do nothing

      case 1 =>
        sb append alphabet((bytes(offset) >>> 3) & 0x1f)
        sb append alphabet((bytes(offset) & 0x7) << 2)

      case 2 =>
        sb append alphabet((bytes(offset) >>> 3) & 0x1f)
        sb append alphabet(
          ((bytes(offset) & 0x7) << 2) | ((bytes(offset + 1) >>> 6) & 0x3)
        )
        sb append alphabet((bytes(offset + 1) >>> 1) & 0x1f)
        sb append alphabet((bytes(offset + 1) & 0x1) << 4)

      case 3 =>
        sb append alphabet((bytes(offset) >>> 3) & 0x1f)
        sb append alphabet(
          ((bytes(offset) & 0x7) << 2) | ((bytes(offset + 1) >>> 6) & 0x3)
        )
        sb append alphabet((bytes(offset + 1) >>> 1) & 0x1f)
        sb append alphabet(
          ((bytes(offset + 1) & 0x1) << 4) | ((bytes(offset + 2) >>> 4) & 0xf)
        )
        sb append alphabet((bytes(offset + 2) & 0xf) << 1)

      case 4 =>
        sb append alphabet((bytes(offset) >>> 3) & 0x1f)
        sb append alphabet(
          ((bytes(offset) & 0x7) << 2) | ((bytes(offset + 1) >>> 6) & 0x3)
        )
        sb append alphabet((bytes(offset + 1) >>> 1) & 0x1f)
        sb append alphabet(
          ((bytes(offset + 1) & 0x1) << 4) | ((bytes(offset + 2) >>> 4) & 0xf)
        )
        sb append alphabet(
          ((bytes(offset + 2) & 0xf) << 1) | ((bytes(offset + 3) >>> 7) & 0x1)
        )
        sb append alphabet((bytes(offset + 3) >>> 2) & 0x1f)
        sb append alphabet((bytes(offset + 3) & 0x3) << 3)

      case _ => // 5 or more
        sb append alphabet((bytes(offset) >>> 3) & 0x1f)
        sb append alphabet(
          ((bytes(offset) & 0x7) << 2) | ((bytes(offset + 1) >>> 6) & 0x3)
        )
        sb append alphabet((bytes(offset + 1) >>> 1) & 0x1f)
        sb append alphabet(
          ((bytes(offset + 1) & 0x1) << 4) | ((bytes(offset + 2) >>> 4) & 0xf)
        )
        sb append alphabet(
          ((bytes(offset + 2) & 0xf) << 1) | ((bytes(offset + 3) >>> 7) & 0x1)
        )
        sb append alphabet((bytes(offset + 3) >>> 2) & 0x1f)
        sb append alphabet(
          ((bytes(offset + 3) & 0x3) << 3) | ((bytes(offset + 4) >>> 5) & 0x7)
        )
        sb append alphabet(bytes(offset + 4) & 0x1f)
    }

  private def base16_b2c(
    byte: Byte,
    alphabet: Array[Char],
    sb: StringBuilder
  ): Unit = {
    sb append alphabet((byte >>> 4) & 0xf)
    sb append alphabet(byte & 0xf)
  }

  /**
   * Encode specified part of array as base64 with specified alphabet
   */
  def base64(
    data: Array[Byte],
    offset: Int,
    len: Int,
    alphabet: Array[Char]
  ): String = {
    val sb = new StringBuilder(4 * (len / 3))
    var i = offset
    val lim = offset + len
    while (i < lim) {
      base64_b2c(data, i, lim - i, alphabet, sb)
      i += 3
    }
    sb.toString()
  }

  /**
   * Encode an array as base64 with specified alphabet
   */
  def base64(data: Array[Byte], alphabet: Array[Char]): String =
    base64(data, 0, data.length, alphabet)

  /**
   * Encode specified part of array as base64 with RFC 4648 Section 4 alphabet
   */
  def base64(data: Array[Byte], offset: Int, len: Int): String =
    base64(data, offset, len, base64_alphabet)

  /**
   * Encode an array as base64 with RFC 4648 Section 4 alphabet
   */
  def base64(data: Array[Byte]): String =
    base64(data, 0, data.length, base64_alphabet)

  /**
   * Encode specified part of array as base64 with RFC 4648 Section 5 alphabet
   */
  def base64_url(data: Array[Byte], offset: Int, len: Int): String =
    base64(data, offset, len, base64_url_alphabet)

  /**
   * Encode an array as base64 with RFC 4648 Section 5 alphabet
   */
  def base64_url(data: Array[Byte]): String =
    base64(data, 0, data.length, base64_url_alphabet)

  /**
   * Encode specified part of array as base32 with specified alphabet
   */
  def base32(
    data: Array[Byte],
    offset: Int,
    len: Int,
    alphabet: Array[Char]
  ): String = {
    val sb = new StringBuilder(8 * (len / 5))
    var i = offset
    val lim = offset + len
    while (i < lim) {
      base32_b2c(data, i, lim - i, alphabet, sb)
      i += 5
    }
    sb.toString()
  }

  /**
   * Encode an array as base32 with specified alphabet
   */
  def base32(data: Array[Byte], alphabet: Array[Char]): String =
    base32(data, 0, data.length, alphabet)

  /**
   * Encode specified part of array as base32 with RFC 4648 Section 6 alphabet
   */
  def base32(data: Array[Byte], offset: Int, len: Int): String =
    base32(data, offset, len, base32_alphabet)

  /**
   * Encode an array as base32 with RFC 4648 Section 6 alphabet
   */
  def base32(data: Array[Byte]): String =
    base32(data, 0, data.length, base32_alphabet)

  /**
   * Encode specified part of array as base32 with RFC 4648 Section 7 alphabet
   */
  def base32_hex(data: Array[Byte], offset: Int, len: Int): String =
    base32(data, offset, len, base32_hex_alphabet)

  /**
   * Encode an array as base32 with RFC 4648 Section 7 alphabet
   */
  def base32_hex(data: Array[Byte]): String =
    base32(data, 0, data.length, base32_hex_alphabet)

  /**
   * Encode specified part of array as base16 with specified alphabet
   */
  def base16(
    data: Array[Byte],
    offset: Int,
    len: Int,
    alphabet: Array[Char]
  ): String = {
    val sb = new StringBuilder(len * 2)
    var i = offset
    val lim = offset + len
    while (i < lim) {
      base16_b2c(data(i), alphabet, sb)
      i += 1
    }
    sb.toString()
  }

  /**
   * Encode an array as base16 with specified alphabet
   */
  def base16(data: Array[Byte], alphabet: Array[Char]): String =
    base16(data, 0, data.length, alphabet)

  /**
   * Encode specified part of array as base16 with RFC 4648 Section 8 alphabet
   */
  def base16(data: Array[Byte], offset: Int, len: Int): String =
    base16(data, offset, len, base16_alphabet)

  /**
   * Encode an array as base16 with RFC 4648 Section 8 alphabet
   */
  def base16(data: Array[Byte]): String =
    base16(data, 0, data.length, base16_alphabet)
}
