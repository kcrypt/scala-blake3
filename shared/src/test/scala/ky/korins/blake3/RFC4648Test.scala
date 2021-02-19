/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

package ky.korins.blake3

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

import scala.language.implicitConversions

class RFC4648Test extends AnyWordSpec with should.Matchers {
  "base16 works on test vector" in {
    RFC4648.base16("".getBytes()) shouldBe ""
    RFC4648.base16("f".getBytes()) shouldBe "66"
    RFC4648.base16("fo".getBytes()) shouldBe "666F"
    RFC4648.base16("foo".getBytes()) shouldBe "666F6F"
    RFC4648.base16("foob".getBytes()) shouldBe "666F6F62"
    RFC4648.base16("fooba".getBytes()) shouldBe "666F6F6261"
    RFC4648.base16("foobar".getBytes()) shouldBe "666F6F626172"
  }

  "base32 works on test vector" in {
    RFC4648.base32("".getBytes()) shouldBe ""
    RFC4648.base32("f".getBytes()) shouldBe "MY"
    RFC4648.base32("fo".getBytes()) shouldBe "MZXQ"
    RFC4648.base32("foo".getBytes()) shouldBe "MZXW6"
    RFC4648.base32("foob".getBytes()) shouldBe "MZXW6YQ"
    RFC4648.base32("fooba".getBytes()) shouldBe "MZXW6YTB"
    RFC4648.base32("foobar".getBytes()) shouldBe "MZXW6YTBOI"
  }

  "base32_hex works on test vector" in {
    RFC4648.base32_hex("".getBytes()) shouldBe ""
    RFC4648.base32_hex("f".getBytes()) shouldBe "CO"
    RFC4648.base32_hex("fo".getBytes()) shouldBe "CPNG"
    RFC4648.base32_hex("foo".getBytes()) shouldBe "CPNMU"
    RFC4648.base32_hex("foob".getBytes()) shouldBe "CPNMUOG"
    RFC4648.base32_hex("fooba".getBytes()) shouldBe "CPNMUOJ1"
    RFC4648.base32_hex("foobar".getBytes()) shouldBe "CPNMUOJ1E8"
  }

  "base64 works on test vector" in {
    RFC4648.base64("".getBytes()) shouldBe ""
    RFC4648.base64("f".getBytes()) shouldBe "Zg"
    RFC4648.base64("fo".getBytes()) shouldBe "Zm8"
    RFC4648.base64("foo".getBytes()) shouldBe "Zm9v"
    RFC4648.base64("foob".getBytes()) shouldBe "Zm9vYg"
    RFC4648.base64("fooba".getBytes()) shouldBe "Zm9vYmE"
    RFC4648.base64("foobar".getBytes()) shouldBe "Zm9vYmFy"
  }

  "base64_url works on test vector" in {
    RFC4648.base64_url("".getBytes()) shouldBe ""
    RFC4648.base64_url("f".getBytes()) shouldBe "Zg"
    RFC4648.base64_url("fo".getBytes()) shouldBe "Zm8"
    RFC4648.base64_url("foo".getBytes()) shouldBe "Zm9v"
    RFC4648.base64_url("foob".getBytes()) shouldBe "Zm9vYg"
    RFC4648.base64_url("fooba".getBytes()) shouldBe "Zm9vYmE"
    RFC4648.base64_url("foobar".getBytes()) shouldBe "Zm9vYmFy"
  }

  "regression in 2.4.0" when {
    "base32" in {
      val bytes = Array(
        0x4e.toByte, 0x53.toByte, 0xec.toByte, 0x55.toByte, 0x6c.toByte,
        0xd3.toByte, 0x70.toByte, 0x97.toByte, 0xa2.toByte, 0xbe.toByte,
        0x4e.toByte, 0x53.toByte, 0xec.toByte, 0x55.toByte, 0x6c.toByte
      )

      val expected = "JZJ6YVLM2NYJPIV6JZJ6YVLM"
      RFC4648.base32(bytes.take(5)) shouldEqual expected.take(8)
      RFC4648.base32(bytes.slice(5, 10)) shouldEqual expected.slice(8, 16)
      RFC4648.base32(bytes.drop(10)) shouldEqual expected.drop(16)

      RFC4648.base32(bytes) shouldEqual expected
    }

    "base64" in {
      val bytes = Array(
        0x4e.toByte, 0x53.toByte, 0xec.toByte,
        0x55.toByte, 0x6c.toByte, 0xd3.toByte,
        0x4e.toByte, 0x53.toByte, 0xec.toByte
      )

      val expected = "TlPsVWzTTlPs"
      RFC4648.base64(bytes.take(3)) shouldEqual expected.take(4)
      RFC4648.base64(bytes.slice(3, 6)) shouldEqual expected.slice(4, 8)
      RFC4648.base64(bytes.drop(3)) shouldEqual expected.drop(4)

      RFC4648.base64(bytes) shouldEqual expected
    }
  }
}
