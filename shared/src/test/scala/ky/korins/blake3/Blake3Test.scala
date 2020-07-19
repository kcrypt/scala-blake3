package ky.korins.blake3

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class Blake3Test extends AnyWordSpec with should.Matchers {
  "work at naive test" in {
    val hash = Blake3.newHasher()
      .doneHex(2)
    "af13" should be(hash)
  }
}

