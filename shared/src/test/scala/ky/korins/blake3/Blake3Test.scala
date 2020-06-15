package ky.korins.blake3

import ky.korins.blake3
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class Blake3Test extends AnyWordSpec with should.Matchers {

  "Blake3" should {
    "work at naive test" in {
      val hash = Blake3.newHasher()
        .doneHex(2)
      "af13" should be(hash)
    }

    "passes authors test vectors" in {
      val TEST_KEY = Blake3TestVectors.testVector.key.getBytes().take(blake3.KEY_LEN)
      val TEST_CONTEXT = "BLAKE3 2019-12-27 16:29:52 test vectors context"
      val OUTPUT_LEN = 2 * blake3.BLOCK_LEN + 3
      lazy val inputStream: LazyList[Byte] = LazyList.range(0, 251).map(_.toByte) #::: inputStream

      for (testCase <- Blake3TestVectors.testVector.cases) {
        val input = inputStream.take(testCase.input_len).toArray
        val hash = Blake3.newHasher()
          .update(input)
          .doneHex(OUTPUT_LEN)
        val keyed_hash = Blake3.newKeyedHasher(TEST_KEY)
          .update(input)
          .doneHex(OUTPUT_LEN)
        val derive_key = Blake3.newDeriveKeyHasher(TEST_CONTEXT)
          .update(input)
          .doneHex(OUTPUT_LEN)
        testCase.hash should be(hash)
        testCase.keyed_hash should be(keyed_hash)
        testCase.derive_key should be(derive_key)
      }
    }
  }
}

