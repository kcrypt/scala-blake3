/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it islicensed under the Apache License 2.0.
 */

package ky.korins.blake3.benchmark

import io.lktk.NativeBLAKE3
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class Blake3JNIBenchmark {
  @Param(Array("16384", "10485760", "104857600"))
  var dataLen: Int = 0
  var data: Array[Byte] = Array()

  @Param(Array("16384", "10485760", "104857600"))
  var hashLen: Int = 0
  var hashBytes: Array[Byte] = Array()

  var hasher: NativeBLAKE3 = _

  @Setup
  def setup(): Unit = {
    assert(NativeBLAKE3.isEnabled)

    hasher = new NativeBLAKE3()
    hasher.initDefault()

    val random = new Random()
    data = Array.fill(dataLen)(0)
    random.nextBytes(data)
  }

  @TearDown
  def tearDown(): Unit = {
    hasher.close()
  }

  @Benchmark
  @Threads(1)
  def singleThreads(): Unit = {
    hasher.update(data)
    hasher.getOutput(hashLen)
  }

  @Benchmark
  @Threads(10)
  def tenThreads(): Unit = {
    hasher.update(data)
    hasher.getOutput(hashLen)
  }

  @Benchmark
  @Threads(100)
  def hundredThreads(): Unit = {
    hasher.update(data)
    hasher.getOutput(hashLen)
  }
}
