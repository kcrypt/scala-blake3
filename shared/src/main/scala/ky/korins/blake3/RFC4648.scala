package ky.korins.blake3

/**
 * Implementing RFC 4648 encoding without padding
 */
object RFC4648 {
  // RFC 4648 Section 4
  private val base64_alphabet: Array[Char] =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray

  // RFC 4648 Section 4
  private val base64_url_alphabet: Array[Char] =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray

  // RFC 4648 Section 6
  private val base32_alphabet: Array[Char] =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray

  // RFC 4648 Section 7
  private val base32_hex_alphabet: Array[Char] =
    "0123456789ABCDEFGHIJKLMNOPQRSTUV".toCharArray

  // RFC 4648 Section 8
  private val base16_alphabet: Array[Char] =
    "0123456789ABCDEF".toCharArray

  private def base64_b2c(bytes: Array[Byte], alphabet: Array[Char]): Seq[Char] =
    bytes.length match {
      case 0 =>
        Seq()

      case 1 =>
        Seq(
          alphabet((bytes(0) >>> 2) & 0x3f),
          alphabet((bytes(0) & 0x3) << 4)
        )

      case 2 =>
        Seq(
          alphabet((bytes(0) >>> 2) & 0x3f),
          alphabet(((bytes(0) & 0x3) << 4) | ((bytes(1) >>> 4) & 0xf)),
          alphabet((bytes(1) & 0xf) << 2)
        )

      case 3 =>
        Seq(
          alphabet((bytes(0) >>> 2) & 0x3f),
          alphabet(((bytes(0) & 0x3) << 4) | ((bytes(1) >>> 4) & 0xf)),
          alphabet(((bytes(1) & 0xf) << 2) | ((bytes(2) >>> 6) & 0x3)),
          alphabet(bytes(2) & 0x3f)
        )

      case s =>
        throw new IllegalArgumentException(
          s"base64 group should be from 0 to 3 bytes, but it is $s"
        )
    }

  private def base32_b2c(bytes: Array[Byte], alphabet: Array[Char]): Seq[Char] =
    bytes.length match {
      case 0 =>
        Seq()

      case 1 =>
        Seq(
          alphabet((bytes(0) >>> 3) & 0x1f),
          alphabet((bytes(0) & 0x7) << 2)
        )

      case 2 =>
        Seq(
          alphabet((bytes(0) >>> 3) & 0x1f),
          alphabet(((bytes(0) & 0x7) << 2) | ((bytes(1) >>> 6) & 0x3)),
          alphabet((bytes(1) >>> 1) & 0x1f),
          alphabet((bytes(1) & 0x1) << 4)
        )

      case 3 =>
        Seq(
          alphabet((bytes(0) >>> 3) & 0x1f),
          alphabet(((bytes(0) & 0x7) << 2) | ((bytes(1) >>> 6) & 0x3)),
          alphabet((bytes(1) >>> 1) & 0x1f),
          alphabet(((bytes(1) & 0x1) << 4) | ((bytes(2) >>> 4) & 0xf)),
          alphabet((bytes(2) & 0xf) << 1)
        )

      case 4 =>
        Seq(
          alphabet((bytes(0) >>> 3) & 0x1f),
          alphabet(((bytes(0) & 0x7) << 2) | ((bytes(1) >>> 6) & 0x3)),
          alphabet((bytes(1) >>> 1) & 0x1f),
          alphabet(((bytes(1) & 0x1) << 4) | ((bytes(2) >>> 4) & 0xf)),
          alphabet(((bytes(2) & 0xf) << 1) | ((bytes(3) >>> 7) & 0x1)),
          alphabet((bytes(3) >>> 2) & 0x1f),
          alphabet((bytes(3) & 0x3) << 3)
        )

      case 5 =>
        Seq(
          alphabet((bytes(0) >>> 3) & 0x1f),
          alphabet(((bytes(0) & 0x7) << 2) | ((bytes(1) >>> 6) & 0x3)),
          alphabet((bytes(1) >>> 1) & 0x1f),
          alphabet(((bytes(1) & 0x1) << 4) | ((bytes(2) >>> 4) & 0xf)),
          alphabet(((bytes(2) & 0xf) << 1) | ((bytes(3) >>> 7) & 0x1)),
          alphabet((bytes(3) >>> 2) & 0x1f),
          alphabet(((bytes(3) & 0x3) << 3) | ((bytes(4) >>> 5) & 0x7)),
          alphabet(bytes(4) & 0x1f)
        )

      case s =>
        throw new IllegalArgumentException(
          s"base32 group should be from 0 to 5 bytes, but it is $s"
        )
    }

  private def base16_b2c(byte: Byte, alphabet: Array[Char]): Seq[Char] = Seq(
    alphabet((byte >>> 4) & 0xf),
    alphabet(byte & 0xf)
  )

  def base64(data: Array[Byte]): String =
    data.grouped(3).flatMap(base64_b2c(_, base64_alphabet)).mkString

  def base64_url(data: Array[Byte]): String =
    data.grouped(3).flatMap(base64_b2c(_, base64_url_alphabet)).mkString

  def base32(data: Array[Byte]): String =
    data.grouped(5).flatMap(base32_b2c(_, base32_alphabet)).mkString

  def base32_hex(data: Array[Byte]): String =
    data.grouped(5).flatMap(base32_b2c(_, base32_hex_alphabet)).mkString

  def base16(data: Array[Byte]): String =
    data.flatMap(base16_b2c(_, base16_alphabet)).mkString
}
