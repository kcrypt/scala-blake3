package ky.korins.blake3.benchmark

import io.lktk.NativeBLAKE3
import org.openjdk.jmh.annotations._

import scala.util.Random

@State(Scope.Benchmark)
class Blake3JNIBenchmark {
  @Param(Array("16384", "10485760", "104857600"))
  var dataLen: Int = 0
  var data: Array[Byte] = Array()

  @Param(Array("128", "256", "1024"))
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
  def hash(): Unit = {
    hasher.update(data)
    hasher.getOutput(hashLen)
  }
}

