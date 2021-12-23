/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * Supported since 2022 by Kcrypt Lab UG <support@kcry.pt>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

package pt.kcry.blake3

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

import scala.language.implicitConversions
import scala.util.Random

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

  "Short works as Byte" in {
    val shortHash = Blake3
      .newHasher()
      .update(0x6af6.toShort)
      .doneLong()

    val byteHash = Blake3
      .newHasher()
      .update(0xf6.toByte)
      .update(0x6a.toByte)
      .doneLong()

    shortHash shouldBe byteHash
  }

  "Int works as Byte" in {
    val shortHash = Blake3
      .newHasher()
      .update(0x7eaa6af6)
      .doneLong()

    val byteHash = Blake3
      .newHasher()
      .update(0xf6.toByte)
      .update(0x6a.toByte)
      .update(0xaa.toByte)
      .update(0x7e.toByte)
      .doneLong()

    shortHash shouldBe byteHash
  }

  "Long works as Byte" in {
    val shortHash = Blake3
      .newHasher()
      .update(0x462d22e57eaa6af6L)
      .doneLong()

    val byteHash = Blake3
      .newHasher()
      .update(0xf6.toByte)
      .update(0x6a.toByte)
      .update(0xaa.toByte)
      .update(0x7e.toByte)
      .update(0xe5.toByte)
      .update(0x22.toByte)
      .update(0x2d.toByte)
      .update(0x46.toByte)
      .doneLong()

    shortHash shouldBe byteHash
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

    Blake3.hashShort(input) shouldBe -24160
    Blake3.hashInt(input) shouldBe 1505862048
    Blake3.hashLong(input) shouldBe 6467628250047457941L

    Blake3.hex(input, 4) shouldBe "a0a1"
    Blake3.base16(input, 4) shouldBe "A0A1C159"

    Blake3.base32(input, 4) shouldBe "UCQ4CWI"
    Blake3.base32Hex(input, 4) shouldBe "K2GS2M8"

    Blake3.base64(input, 4) shouldBe "oKHBWQ"
    Blake3.base64Url(input, 4) shouldBe "oKHBWQ"

    Blake3.bigInt(input, 16).toString() shouldBe "41121"
    Blake3.bigInt(input, BigInt(41119)).toString() shouldBe "2"
  }

  "XORed output" in {
    val hashed = Blake3
      .newHasher()
      .update("XXX")
      .done(32)

    val hashedXor = hashed.indices.map(_.toByte).toArray
    Blake3.newHasher().update("XXX").doneXor(hashedXor)

    hashed.indices.foreach { i =>
      hashedXor(i) shouldBe (hashed(i) ^ i).toByte
    }
  }

  "Zero done" in {
    Blake3.hash("Some string", 0) shouldBe Array[Byte]()
  }

  "Incorrect input reported" when {
    "wrong key length on Blake3.newKeyedHasher" in {
      the[IllegalArgumentException] thrownBy {
        Blake3.newKeyedHasher("42".getBytes())
      } should have message "key should be pt.kcry.blake3.KEY_LEN: 32 bytes"
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

  "Chunking data" when {
    "63 bytes" in {
      val bytes = new Array[Byte](63)
      Random.nextBytes(bytes)
      val hasher1 = Blake3.newHasher()
      hasher1.update(bytes)
      val expected = hasher1.doneHex(16)

      val hasher2 = Blake3.newHasher()
      hasher2.update(bytes, 0, 32)
      hasher2.update(bytes, 32, 31)
      val actual = hasher2.doneHex(16)

      expected shouldBe actual
    }

    "64 bytes" in {
      val bytes = new Array[Byte](64)
      Random.nextBytes(bytes)
      val hasher1 = Blake3.newHasher()
      hasher1.update(bytes, 0, 64)
      val expected = hasher1.doneHex(16)

      val hasher2 = Blake3.newHasher()
      hasher2.update(bytes, 0, 32)
      hasher2.update(bytes, 32, 32)
      val actual = hasher2.doneHex(16)

      expected shouldBe actual
    }
  }

  "Regression in 2.6.0" in {
    val bytes = new Array[Byte](146)
    Random.nextBytes(bytes)
    val expected = Blake3.base16(bytes, 32)
    val hasher = Blake3.newHasher()
    hasher.update(bytes, 0, 125)
    hasher.update(bytes, 125, 21)
    val actual = hasher.doneBase16(32)
    expected shouldBe actual
  }
}
