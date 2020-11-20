package ky.korins.blake3.benchmark

import ky.korins.blake3._
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class Blake3Benchmark {
  @Param(Array("16384", "10485760", "104857600"))
  var dataLen: Int = 0
  var data: Array[Byte] = Array()

  @Param(Array("16384", "10485760", "104857600"))
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
  @Threads(1)
  def singleThreads(): Unit =
    hasher
      .update(data)
      .done(hashBytes)

  @Benchmark
  @Threads(10)
  def tenThreads(): Unit =
    hasher
      .update(data)
      .done(hashBytes)

  @Benchmark
  @Threads(100)
  def hundredThreads(): Unit =
    hasher
      .update(data)
      .done(hashBytes)
}

object Blake3Benchmark extends App {
  val benchmark = new Blake3Benchmark()
  benchmark.dataLen = 10485760
  benchmark.hashLen = 10485760
  benchmark.setup()

  while (true) {
    benchmark.singleThreads()
  }
}
