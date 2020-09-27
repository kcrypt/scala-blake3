package ky.korins.blake3.benchmark

import ky.korins.blake3._
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class Blake3Benchmark {
  @Param(Array("0", "64", "256", "1024", "16384"))
  var dataLen: Int = 0
  var data: Array[Byte] = Array()

  @Param(Array("32", "64", "128", "256"))
  var hashLen: Int = 0
  var hashBytes: Array[Byte] = Array()

  var hasher: Hasher = _

  @Setup
  def setup(): Unit = {
    val random = new Random()
    data = Array.fill(dataLen)(0)
    random.nextBytes(data)
    hashBytes = Array.fill[Byte](hashLen)(0)
    hasher = Blake3.newHasher()
  }

  @Benchmark
  def hash(): Unit =
    Blake3.newHasher()
      .update(data)
      .done(hashBytes)

  @Benchmark
  def concurrentHash(): Unit =
    hasher
      .update(data)
      .done(hashBytes)
}
