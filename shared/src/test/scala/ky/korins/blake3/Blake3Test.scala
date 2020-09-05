package ky.korins.blake3

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class Blake3Test extends AnyWordSpec with should.Matchers {
  "Very naive test" in {
    val hash = Blake3.newHasher()
      .doneHex(2)
    "af13" should be(hash)
  }

  "A bit more complicated test" in {
    val hash = Blake3.newHasher()
      .update("a")
      .update("b")
      .update("c")
      .doneHex(3)

    val oneShop = Blake3.newHasher()
      .update("abc")
      .doneHex(3)

    oneShop should be(hash)
  }

  "Update from specified position" in {
    val pattern = "some test"
    val extended = s"xxx${pattern}xxx"

    val hashedPattern = Blake3.newHasher()
      .update(pattern)
      .done(13)

    val hashedExtended = Blake3.newHasher()
      .update(extended.getBytes, 3, pattern.length)
      .done(13)

    val hashedIncorrect = Blake3.newHasher()
      .update(extended.getBytes, 2, pattern.length)
      .done(13)

    hashedExtended should be(hashedPattern)
    hashedIncorrect shouldNot be(hashedPattern)
  }


  "Done to specified position" in {
    val pattern = "some test"

    val hash = Blake3.newHasher()
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
}

