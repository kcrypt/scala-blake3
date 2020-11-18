package ky.korins.blake3.benchmark

import org.bouncycastle.jcajce.provider.digest.SHA3
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class BouncyCastleBenchmark {
  @Param(Array("0", "64", "256", "1024", "16384", "10485760", "104857600"))
  var dataLen: Int = 0
  var data: Array[Byte] = Array()

  @Setup
  def setup(): Unit = {
    val random = new Random()
    data = Array.fill(dataLen)(0)
    random.nextBytes(data)
  }

  @Benchmark
  def sha3_512(): Unit = {
    val digestSHA3 = new SHA3.Digest512();
    digestSHA3.digest(data)
  }

  @Benchmark
  def sha3_256(): Unit = {
    val digestSHA3 = new SHA3.Digest256();
    digestSHA3.digest(data)
  }
}
