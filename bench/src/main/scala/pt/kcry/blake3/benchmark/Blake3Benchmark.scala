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
package benchmark

import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class Blake3Benchmark {
  @Param(Array("16384", "10485760"))
  var dataLen: Int = 0
  var data: Array[Byte] = Array()

  @Param(Array("256", "16384", "10485760"))
  var hashLen: Int = 0
  var hashBytes: Array[Byte] = Array()

  var hasher: Hasher = _

  @Setup
  def setup(): Unit = {
    val random = new Random()
    data = new Array[Byte](dataLen)
    random.nextBytes(data)
    hashBytes = new Array[Byte](hashLen)
    hasher = Blake3.newHasher()
  }

  @Benchmark
  def newHasher(): Unit =
    Blake3
      .newHasher()
      .update(data)
      .done(hashBytes)

  @Benchmark
  def reusedHasher(): Unit =
    hasher
      .update(data)
      .done(hashBytes)
}
