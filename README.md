# Blake3 for scala

This is highly optimized blake3 implementation for scala, scala-js and scala-native.

You can use it as
```
libraryDependencies += "ky.korins" %%% "blake3" % "x.x.x"
```
The latest version is ![maven-central]

API is pretty simple:
```
scala> import ky.korins.blake3.Blake3

scala> Blake3.newHasher().update("Some string").doneHex(64)
val res1: String = 2e5524f3481046587080604ae4b4ceb44b721f3964ce0764627dee2c171de4c2

scala> Blake3.newDeriveKeyHasher("whats the Elvish word for friend").update("Some string").doneHex(64)
val res2: String = c2e79fe73dde16a13b4aa5a947b0e9cd7277ea8e68da250759de3ae62372b340

scala> Blake3.newKeyedHasher("whats the Elvish word for friend").update("Some string").doneHex(64)
val res3: String = 79943402309f9bb05338193f21fb57d98ab848bdcac67e5e097340f116ff90ba

scala> Blake3.hex("Some string", 64)
val res4: String = 2e5524f3481046587080604ae4b4ceb44b721f3964ce0764627dee2c171de4c2

scala> Blake3.bigInt("Some string", 32)
val res5: BigInt = 777331955

scala> 
```

`Hasher.update` is mutable when `Hasher.done` isn't.

`Hasher.update` supports different input such as: byte array, part of byte array, single byte or string.

`Hasher.done` supports different output such as:
 - `done(out: Array[Byte])` to fill full provided array;
 - `done(out: Array[Byte], offset: Int, len: Int)` to fill specified part of provided array;
 - `done()` that returns a single byte hash value;
 - `doneBigInt(bitLength: Int)` that returns positive BigInt with specified length in bits;
 - `doneHex(resultLength: Int)` that returns hex encoded string with specified output length in characters;
 - `doneBaseXXX(len: Int)` that returns string representative of XXX encoded as it defined in [RFC 4648] without padding.
 
This implementation is thread-safe and you can use it in multithreaded environment.
Anyway this implementation doesn't currently include any multithreading optimizations.

As baseline for benchmarks I've used [BLAKE3jni] that is used original C version [c-0.3.7],
 that includes a lot of performance optimizations likes [SSE 4.1 assembly] version,
 but this way introduced one limitation: unfortunately I can't measure memory footprint,
 because it is managed by original library.

All benchmarks was performed on `JDK 15.0.1, OpenJDK 64-Bit Server VM, 15.0.1+9-18`
 at [Intel® Core™ i7-8700B] from Q2'18.

Short summary:
 - it is 10 times slower than JNI version,
 - it has memory footprint near 70% of hashed data,
 - it has near to constant memory footprint that won't be cleaned up by GC,
 - result hash size hasn't got any significant impact on performance or memory footprint.

Full version also available as [jmh-result.json] or via [JMH Visualizer]

[maven-central]: https://img.shields.io/maven-central/v/ky.korins/blake3_2.13?style=flat-square
[RFC 4648]: https://tools.ietf.org/html/rfc4648
[BLAKE3jni]: https://github.com/sken77/BLAKE3jni
[SSE 4.1 assembly]: https://github.com/BLAKE3-team/BLAKE3/blob/master/c/blake3_sse41_x86-64_unix.S
[c-0.3.7]: https://github.com/BLAKE3-team/BLAKE3/releases/tag/c-0.3.7
[Intel® Core™ i7-8700B]: https://ark.intel.com/content/www/us/en/ark/products/134905/intel-core-i7-8700b-processor-12m-cache-up-to-4-60-ghz.html
[jmh-result.json]: jmh-result.json
[JMH Visualizer]: https://jmh.morethan.io/?source=https://raw.githubusercontent.com/catap/scala-blake3/master/jmh-result.json