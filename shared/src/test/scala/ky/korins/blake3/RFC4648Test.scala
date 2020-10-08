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
}
