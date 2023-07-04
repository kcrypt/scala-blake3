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

import pt.kcry.blake3
import org.scalatest.Assertion
import org.scalatest.matchers.should

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import scala.language.implicitConversions

class TestVector(
  val testKey: String, val testContext: String, val inputLimit: Int = 251
) extends should.Matchers {

  final val updateMethods: Seq[(Hasher, Array[Byte]) => Unit] = Seq(
    (hasher, input) => hasher.update(input),
    (hasher, input) => input.foreach(hasher.update),
    { (hasher, input) =>
      val bos = new ByteArrayInputStream(input)
      hasher.update(bos)
    },
    { (hasher, input) =>
      val bb = ByteBuffer.wrap(input)
      hasher.update(bb)
    },
    { (hasher, input) =>
      val bb = ByteBuffer.allocateDirect(input.length + 1)
      bb.put(0.toByte)
      bb.put(input)
      bb.flip()
      bb.get() // skip 0
      hasher.update(bb)
    }
  )

  private def byte2hex(bytes: Array[Byte]): String = RFC4648.base16(bytes)
    .toLowerCase

  final val doneMethods: Seq[(Hasher, Int) => String] = Seq(
    (hasher, outputLen) => byte2hex(hasher.done(outputLen)),
    { (hasher, outputLen) =>
      val out = (0 until outputLen).map(_.toByte).toArray
      hasher.doneXor(out)
      out.indices.foreach(i => out(i) = (out(i) ^ i.toByte).toByte)
      byte2hex(out)
    },
    (hasher, outputLen) => hasher.doneBase16(outputLen).toLowerCase,
    (hasher, outputLen) => hasher.doneHex(2 * outputLen),
    { (hasher, outputLen) =>
      val bos = new ByteArrayOutputStream()
      hasher.done(bos, outputLen)
      byte2hex(bos.toByteArray)
    },
    { (hasher, outputLen) =>
      val mask = (0 until outputLen).map(_.toByte).toArray
      val bis = new ByteArrayInputStream(mask)
      val bos = new ByteArrayOutputStream()
      hasher.doneXor(bis, bos, outputLen)
      val out = bos.toByteArray
      mask.indices.foreach(i => out(i) = (out(i) ^ i.toByte).toByte)
      byte2hex(out)
    },
    { (hasher, outputLen) =>
      val bb = ByteBuffer.allocate(outputLen)
      hasher.done(bb)
      byte2hex(bb.array())
    },
    { (hasher, outputLen) =>
      val bb = ByteBuffer.allocateDirect(outputLen + 1)
      bb.put(0.toByte)
      hasher.done(bb)
      val bytes = new Array[Byte](outputLen)
      bb.flip()
      bb.get() // skip 0
      bb.get(bytes)
      byte2hex(bytes)
    },
    { (hasher, outputLen) =>
      val mask = (0 until outputLen).map(_.toByte).toArray
      val in = ByteBuffer.wrap(mask)
      val bb = ByteBuffer.allocateDirect(outputLen)
      hasher.doneXor(in, bb, outputLen)
      val out = new Array[Byte](outputLen)
      bb.flip()
      bb.get(out)
      mask.indices.foreach(i => out(i) = (out(i) ^ i.toByte).toByte)
      byte2hex(out)
    },
    { (hasher, outputLen) =>
      val out = ByteBuffer.allocate(outputLen)
      hasher.doneCallBack(out.put, outputLen)
      byte2hex(out.array())
    },
    { (hasher, outputLen) =>
      val mask = (0 until outputLen).map(_.toByte).toArray
      val in = ByteBuffer.wrap(mask)
      val bb = ByteBuffer.allocate(outputLen)
      hasher.doneXorCallBack(in.get, bb.put, outputLen)
      val out = new Array[Byte](outputLen)
      mask.indices.foreach(i => out(i) = (bb.get(i) ^ i.toByte).toByte)
      byte2hex(out)
    }
  )

  def runCase(
    inputLen: Int, outputLen: Int, hash: String, keyedHash: String,
    deriveKeyHash: String
  ): Seq[Assertion] = {
    val input = (0 until inputLen).map(_ % inputLimit).map(_.toByte).toArray
    for {
      update <- updateMethods
      done <- doneMethods
    } yield {
      val hasher = Blake3.newHasher()
      val keyedHasher = Blake3.newKeyedHasher(testKey
        .getBytes(StandardCharsets.US_ASCII).take(blake3.KEY_LEN))
      val deriveHeyHasher = Blake3.newDeriveKeyHasher(testContext)

      update(hasher, input)
      update(keyedHasher, input)
      update(deriveHeyHasher, input)

      done(hasher, outputLen) should be(hash)
      done(keyedHasher, outputLen) should be(keyedHash)
      done(deriveHeyHasher, outputLen) should be(deriveKeyHash)
    }
  }

  def runSquareCase(len: Int): Assertion = {
    val input = (0 until len).map(_ % inputLimit).map(_.toByte).toArray
    val hashers: Seq[((Hasher, Int) => String, (Hasher, Hasher, Hasher))] =
      for {
        update <- updateMethods
        done <- doneMethods
      } yield {
        val hasher = Blake3.newHasher()
        val keyedHasher = Blake3.newKeyedHasher(testKey
          .getBytes(StandardCharsets.US_ASCII).take(blake3.KEY_LEN))
        val deriveKeyHasher = Blake3.newDeriveKeyHasher(testContext)

        update(hasher, input)
        update(keyedHasher, input)
        update(deriveKeyHasher, input)

        (done, (hasher, keyedHasher, deriveKeyHasher))
      }

    val hashes: Seq[(String, String, String)] = hashers
      .map { case (done, (hasher, keyedHasher, deriveHeyHasher)) =>
        (done(hasher, len), done(keyedHasher, len), done(deriveHeyHasher, len))
      }

    val reducer: (
      ((String, String, String), (String, String, String)) => (
        String, String, String
      )
    ) = {
      case ((leftHash: String, leftKeyedHash: String, leftDeriveKeyHash: String),
            (rightHash: String, rightKeyedHash: String,
              rightDeriveKeyHash: String)) =>
        leftHash shouldBe rightHash
        leftKeyedHash shouldBe rightKeyedHash
        leftDeriveKeyHash shouldBe rightDeriveKeyHash

        (rightHash, rightKeyedHash, rightDeriveKeyHash)
    }

    val (hash: String, keyedHash: String, deriveKeyHash: String) = hashes
      .reduce(reducer)

    hash.length shouldBe 2 * len
    keyedHash.length shouldBe 2 * len
    deriveKeyHash.length shouldBe 2 * len
  }
}
