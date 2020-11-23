package ky.korins.blake3

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

import scala.language.implicitConversions

class Blake3Test extends AnyWordSpec with should.Matchers {
  "Very naive test" in {
    val hash = Blake3
      .newHasher()
      .doneHex(4)
    "af13" should be(hash)
  }

  "A bit more complicated test" in {
    val hash = Blake3
      .newHasher()
      .update("a")
      .update("b")
      .update("c")
      .doneHex(4)

    val oneShop = Blake3
      .newHasher()
      .update("abc")
      .doneHex(4)

    oneShop should be(hash)
  }

  "Update from specified position" in {
    val pattern = "some test"
    val extended = s"xxx${pattern}xxx"

    val hashedPattern = Blake3
      .newHasher()
      .update(pattern)
      .done(13)

    val hashedExtended = Blake3
      .newHasher()
      .update(extended.getBytes, 3, pattern.length)
      .done(13)

    val hashedIncorrect = Blake3
      .newHasher()
      .update(extended.getBytes, 2, pattern.length)
      .done(13)

    hashedExtended should be(hashedPattern)
    hashedIncorrect shouldNot be(hashedPattern)
  }

  "Done to specified position" in {
    val pattern = "some test"

    val hash = Blake3
      .newHasher()
      .update(pattern)

    val result = new Array[Byte](13)
    val extendedResult = new Array[Byte](19)

    hash.done(result)
    for (i <- extendedResult.indices) {
      extendedResult(i) = i.toByte
    }
    hash.done(extendedResult, 3, 13)

    val expected = Array[Byte](0, 1, 2) ++ result ++ Array[Byte](16, 17, 18)

    extendedResult should be(expected)
  }

  "High level string works as low level bytes" in {
    val key = "Some key"
    val nextValue = "Some next value"

    val stringHash = Blake3
      .newDeriveKeyHasher(key)
      .update(nextValue)
      .done(42)

    val bytesHash = Blake3
      .newDeriveKeyHasher(key.getBytes)
      .update(nextValue.getBytes)
      .done(42)

    stringHash shouldBe bytesHash

    Blake3.hash(key, 42) shouldBe Blake3.hash(key.getBytes, 42)
    Blake3.hex(key, 42) shouldBe Blake3.hex(key.getBytes, 42)
  }

  "Output encoding works" in {
    val input = "some text"

    Blake3.hash(input) shouldBe 0xa0.toByte
    Blake3.hash(input, 4) shouldBe Array(
      0xa0.toByte,
      0xa1.toByte,
      0xc1.toByte,
      0x59.toByte
    )

    Blake3.hex(input, 4) shouldBe "a0a1"
    Blake3.base16(input, 4) shouldBe "A0A1C159"

    Blake3.base32(input, 4) shouldBe "UCQ4CWI"
    Blake3.base32Hex(input, 4) shouldBe "K2GS2M8"

    Blake3.base64(input, 4) shouldBe "oKHBWQ"
    Blake3.base64Url(input, 4) shouldBe "oKHBWQ"

    Blake3.bigInt(input, 16).toString() shouldBe "41121"
  }

  "Zero done" in {
    Blake3.hash("Some string", 0) shouldBe Array[Byte]()
  }

  "Incorrect input reported" when {
    "wrong key length on Blake3.newKeyedHasher" in {
      the[IllegalArgumentException] thrownBy {
        Blake3.newKeyedHasher("42".getBytes())
      } should have message "key should be ky.korins.blake3.KEY_LEN: 32 bytes"
    }

    "wrong key length on Hasher#doneBigInt" in {
      the[IllegalArgumentException] thrownBy {
        Blake3.bigInt("42", 37)
      } should have message "bitLength: 37 should be a multiple of 8"
    }

    "wrong key length on Hasher#doneHex" in {
      the[IllegalArgumentException] thrownBy {
        Blake3.hex("42", 37)
      } should have message "resultLength: 37 should be even"
    }
  }

  "Infinity loop inside rootByte when offste is bigger than chunk" in {
    val hasher = Blake3.newHasher()
    var chunks = 4
    val bytes = new Array[Byte](chunks * CHUNK_LEN)
    var len = bytes.length
    var off = 0
    while (chunks > 0) {
      hasher.done(bytes, off, len)
      hasher.update(bytes, off, len)
      chunks -= 1
      off += CHUNK_LEN
      len -= CHUNK_LEN
    }
  }
}
