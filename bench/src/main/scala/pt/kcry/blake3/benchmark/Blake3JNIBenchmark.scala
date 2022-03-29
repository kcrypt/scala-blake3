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

package pt.kcry.blake3.benchmark

import org.openjdk.jmh.annotations._

import java.nio.{ByteBuffer, ByteOrder, DirectByteBuffer}
import scala.util.Random

@State(Scope.Benchmark)
class Blake3JNIBenchmark {
  @Param(Array("16384", "10485760"))
  var dataLen: Int = 0
  var data: ByteBuffer = _

  @Param(Array("256", "16384", "10485760"))
  var hashLen: Int = 0
  var hashBytes: ByteBuffer = _

  var hasher: Long = _

  @Setup
  def setup(): Unit = {
    hasher = Blake3JNI.create()
    Blake3JNI.init(hasher)

    val random = new Random()
    val randomData = new Array[Byte](dataLen)
    random.nextBytes(randomData)
    data = ByteBuffer.allocateDirect(dataLen)
    data.put(randomData)
    hashBytes = ByteBuffer.allocateDirect(hashLen)
  }

  @Benchmark
  def newHasher(): Unit = {
    val hasher = Blake3JNI.create()
    Blake3JNI.init(hasher)
    Blake3JNI.update(hasher, data, dataLen)
    Blake3JNI.done(hasher, hashBytes, hashLen)
    Blake3JNI.destroy(hasher)
  }

  @Benchmark
  def reusedHasher(): Unit = {
    Blake3JNI.update(hasher, data, dataLen)
    Blake3JNI.done(hasher, hashBytes, hashLen)
  }
}

/**
 * [[io.lktk.NativeBLAKE3]] contains a lot of allocation that make results
 * unfair.
 *
 * Because C implementation of blake3 is single thread `update` and `done`
 * contains JVM level synchronized
 *
 * This object implement limited version for this benchmark via io.lktk.JNI
 *
 * `libblake3.dylib` should be produced by commands like:
 * {{{
 * clang -I$JAVA_HOME/include -I$JAVA_HOME/include/darwin -arch x86_64 -fPIC -shared -O3 -o libblake3.x86_64.dylib blake3.c blake3_dispatch.c blake3_portable.c blake3_sse41_x86-64_unix.S blake3_avx2_x86-64_unix.S blake3_avx512_x86-64_unix.S io_lktk_JNI.c
 * clang -I$JAVA_HOME/include -I$JAVA_HOME/include/darwin -arch arm64 -fPIC -shared -O3 -o libblake3.arm64.dylib blake3.c blake3_dispatch.c blake3_portable.c io_lktk_JNI.c
 * lipo -arch arm64 libblake3.arm64.dylib -arch x86_64 libblake3.x86_64.dylib -create -output libblake3.dylib
 * }}}
 */
object Blake3JNI {
  import org.scijava.nativelib.NativeLoader
  NativeLoader.loadLibrary("blake3")

  private val jniCls = Class.forName("io.lktk.JNI");

  private val createHasherMethod = jniCls.getDeclaredMethod("create_hasher");
  createHasherMethod.setAccessible(true)

  private val destroyHasherMethod = jniCls.getDeclaredMethod("destroy_hasher",
    classOf[Long]);
  destroyHasherMethod.setAccessible(true)

  private val blake3HasherInitMethod = jniCls
    .getDeclaredMethod("blake3_hasher_init", classOf[Long]);
  blake3HasherInitMethod.setAccessible(true)

  private val blake3HasherUpdateMethod = jniCls
    .getDeclaredMethod("blake3_hasher_update", classOf[Long],
      classOf[ByteBuffer], classOf[Int]);
  blake3HasherUpdateMethod.setAccessible(true)

  private val blake3HasherFinalize = jniCls
    .getDeclaredMethod("blake3_hasher_finalize", classOf[Long],
      classOf[ByteBuffer], classOf[Int]);
  blake3HasherFinalize.setAccessible(true)

  def create(): Long = {
    val hasher = createHasherMethod.invoke(null).asInstanceOf[Long]
    assert(hasher != 0, "create_hasher returns: " + hasher)
    hasher
  }

  def destroy(hasher: Long): Unit = destroyHasherMethod.invoke(null, hasher)

  def init(hasher: Long): Unit = blake3HasherInitMethod.invoke(null, hasher)

  def update(hasher: Long, data: ByteBuffer, len: Int): Unit =
    synchronized(blake3HasherUpdateMethod.invoke(null, hasher, data, len))

  def done(hasher: Long, output: ByteBuffer, len: Int): Unit =
    synchronized(blake3HasherFinalize.invoke(null, hasher, output, len))
}
