package ky.korins.blake3

import org.scalatest.{Matchers, WordSpec}

class Blake3Test extends WordSpec with Matchers {

  "Blake3" should {
    "work at naive test" in {
      val hash = Blake3.newHasher()
        .finalizeHex(2)
      "af13" should be(hash)
    }

    "passes authors test vectors" in {
      val TEST_KEY = Blake3TestVectors.testVectors.key.getBytes().take(Blake3.KEY_LEN)
      val TEST_CONTEXT = "BLAKE3 2019-12-27 16:29:52 test vectors context"
      val OUTPUT_LEN = 2 * Blake3.BLOCK_LEN + 3
      lazy val inputStream: Stream[Byte] = Stream.range(0, 251).map(_.toByte) #::: inputStream

      for (testCase <- Blake3TestVectors.testVectors.cases) {
        val input = inputStream.take(testCase.input_len).toArray
        val hash = Blake3.newHasher()
          .update(input)
          .finalizeHex(OUTPUT_LEN)
        val keyed_hash = Blake3.newKeyedHasher(TEST_KEY)
          .update(input)
          .finalizeHex(OUTPUT_LEN)
        val derive_key = Blake3.newDeriveKeyHasher(TEST_CONTEXT)
          .update(input)
          .finalizeHex(OUTPUT_LEN)
        testCase.hash should be(hash)
        testCase.keyed_hash should be(keyed_hash)
        testCase.derive_key should be(derive_key)
      }
    }
  }
}

