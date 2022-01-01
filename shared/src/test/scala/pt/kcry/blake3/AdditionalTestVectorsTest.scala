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

import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

class AdditionalTestVectorsTest extends AnyWordSpec with should.Matchers {
  "All possibly bytes where inputLen" when {
    val testVector = new TestVector(
      testKey =
        "Ao largo Ainda arde a barca da fantasia o meu sonho acaba tarde deixa a alma de vigia",
      testContext =
        "Scala-BLAKE3 2020-11-21 17:34:42 additional test vectors context",
      inputLimit = 255
    )

    "1024" in {
      testVector.runCase(
        inputLen = 1024,
        outputLen = 32,
        hash =
          "3967a51c259d8262df62f586891923030f98ebefe67f867a11a3e8db8e11f957",
        keyedHash =
          "c9807ec8f612f5e700176f9fe005e82e4180eeb4c660f4fe84ffff948f5d36ed",
        deriveKeyHash =
          "f56c3da85c7c266ddf811a26d7381ae7420505d27362ee79c5157bfd240e249d"
      )
      testVector.runSquareCase(1024)
    }

    "2048" in {
      testVector.runCase(
        inputLen = 2048,
        outputLen = 32,
        hash =
          "a33417bf516bd6a9fa01bed9c70780cf8ae48f864f021ca72275844ad458cf1e",
        keyedHash =
          "5dcc76347ef09ff4bd0062d41e8553badf765d5211aa84171bc94aa8fba075e9",
        deriveKeyHash =
          "8c12563793ea5ee026fa3e44276f497a29d99940dd32862f93f97170c71ffcdc"
      )
      testVector.runSquareCase(2048)
    }

    "16665" in {
      testVector.runCase(
        inputLen = 16665,
        outputLen = 32,
        hash =
          "d3d6c3a478b4110511356fc16827092922f675ace34bc37a2cb2b30dc18a6145",
        keyedHash =
          "b5eb4aae1fe88b0b9ad4afdc0f29ebd64edda72608d4fca854f06b9831b55bed",
        deriveKeyHash =
          "2773641596d84c2384a5239256dc20f3910efe1f239bbcb91f91906457d691c5"
      )
      testVector.runSquareCase(16665)
    }

    "123456" in {
      testVector.runCase(
        inputLen = 123456,
        outputLen = 32,
        hash =
          "fe0122766f6f28a77e3552a095d5f79d32f4bbc354f5a0d84a06259b3a50e9eb",
        keyedHash =
          "10e6f69a82fb36017b17aa26d6d62011400c833c03d71a9c880b76e17fe15260",
        deriveKeyHash =
          "c0065f11a49d9508d1645f3a2bba64644ecf91fdda02a0293777901295ad4ecc"
      )
      testVector.runSquareCase(123456)
    }
  }
}
