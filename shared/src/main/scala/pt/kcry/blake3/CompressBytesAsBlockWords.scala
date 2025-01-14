/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * Supported since 2022 by Kcrypt Lab UG <opensource@kcry.pt>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

package pt.kcry.blake3

private[blake3] object CompressBytesAsBlockWords {
  def compressBytesAsBlockWords(
      bytes: Array[Byte], bytesOffset: Int, tmpBlockWords: Array[Int]
  ): Unit = {
    tmpBlockWords(0) = ((bytes(3 + bytesOffset) & 0xff) << 24) |
      ((bytes(2 + bytesOffset) & 0xff) << 16) |
      ((bytes(1 + bytesOffset) & 0xff) << 8) | bytes(0 + bytesOffset) & 0xff

    tmpBlockWords(1) = ((bytes(7 + bytesOffset) & 0xff) << 24) |
      ((bytes(6 + bytesOffset) & 0xff) << 16) |
      ((bytes(5 + bytesOffset) & 0xff) << 8) | bytes(4 + bytesOffset) & 0xff

    tmpBlockWords(2) = ((bytes(11 + bytesOffset) & 0xff) << 24) |
      ((bytes(10 + bytesOffset) & 0xff) << 16) |
      ((bytes(9 + bytesOffset) & 0xff) << 8) | bytes(8 + bytesOffset) & 0xff

    tmpBlockWords(3) = ((bytes(15 + bytesOffset) & 0xff) << 24) |
      ((bytes(14 + bytesOffset) & 0xff) << 16) |
      ((bytes(13 + bytesOffset) & 0xff) << 8) | bytes(12 + bytesOffset) & 0xff

    tmpBlockWords(4) = ((bytes(19 + bytesOffset) & 0xff) << 24) |
      ((bytes(18 + bytesOffset) & 0xff) << 16) |
      ((bytes(17 + bytesOffset) & 0xff) << 8) | bytes(16 + bytesOffset) & 0xff

    tmpBlockWords(5) = ((bytes(23 + bytesOffset) & 0xff) << 24) |
      ((bytes(22 + bytesOffset) & 0xff) << 16) |
      ((bytes(21 + bytesOffset) & 0xff) << 8) | bytes(20 + bytesOffset) & 0xff

    tmpBlockWords(6) = ((bytes(27 + bytesOffset) & 0xff) << 24) |
      ((bytes(26 + bytesOffset) & 0xff) << 16) |
      ((bytes(25 + bytesOffset) & 0xff) << 8) | bytes(24 + bytesOffset) & 0xff

    tmpBlockWords(7) = ((bytes(31 + bytesOffset) & 0xff) << 24) |
      ((bytes(30 + bytesOffset) & 0xff) << 16) |
      ((bytes(29 + bytesOffset) & 0xff) << 8) | bytes(28 + bytesOffset) & 0xff

    tmpBlockWords(8) = ((bytes(35 + bytesOffset) & 0xff) << 24) |
      ((bytes(34 + bytesOffset) & 0xff) << 16) |
      ((bytes(33 + bytesOffset) & 0xff) << 8) | bytes(32 + bytesOffset) & 0xff

    tmpBlockWords(9) = ((bytes(39 + bytesOffset) & 0xff) << 24) |
      ((bytes(38 + bytesOffset) & 0xff) << 16) |
      ((bytes(37 + bytesOffset) & 0xff) << 8) | bytes(36 + bytesOffset) & 0xff

    tmpBlockWords(10) = ((bytes(43 + bytesOffset) & 0xff) << 24) |
      ((bytes(42 + bytesOffset) & 0xff) << 16) |
      ((bytes(41 + bytesOffset) & 0xff) << 8) | bytes(40 + bytesOffset) & 0xff

    tmpBlockWords(11) = ((bytes(47 + bytesOffset) & 0xff) << 24) |
      ((bytes(46 + bytesOffset) & 0xff) << 16) |
      ((bytes(45 + bytesOffset) & 0xff) << 8) | bytes(44 + bytesOffset) & 0xff

    tmpBlockWords(12) = ((bytes(51 + bytesOffset) & 0xff) << 24) |
      ((bytes(50 + bytesOffset) & 0xff) << 16) |
      ((bytes(49 + bytesOffset) & 0xff) << 8) | bytes(48 + bytesOffset) & 0xff

    tmpBlockWords(13) = ((bytes(55 + bytesOffset) & 0xff) << 24) |
      ((bytes(54 + bytesOffset) & 0xff) << 16) |
      ((bytes(53 + bytesOffset) & 0xff) << 8) | bytes(52 + bytesOffset) & 0xff

    tmpBlockWords(14) = ((bytes(59 + bytesOffset) & 0xff) << 24) |
      ((bytes(58 + bytesOffset) & 0xff) << 16) |
      ((bytes(57 + bytesOffset) & 0xff) << 8) | bytes(56 + bytesOffset) & 0xff

    tmpBlockWords(15) = ((bytes(63 + bytesOffset) & 0xff) << 24) |
      ((bytes(62 + bytesOffset) & 0xff) << 16) |
      ((bytes(61 + bytesOffset) & 0xff) << 8) | bytes(60 + bytesOffset) & 0xff
  }
}
