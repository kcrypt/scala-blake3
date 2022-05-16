# Blake3 for scala

This is highly optimized blake3 implementation for scala, scala-js and scala-native, without any dependencies.
This implementation has constant memory footprint (about 5kb) which hasn't depends on size of hashed data
nor output size hash size.

If you're looking for the faster possible hash function for scala.js I suggest to use this one,
instead of [SHA] because this implementation use only 32 bits number which nativly supported by JS.

You can use it as
```
libraryDependencies += "pt.kcry" %%% "blake3" % "x.x.x"
```
The latest version is ![maven-central]

API is pretty simple:
```
scala> import pt.kcry.blake3.Blake3

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
 - `done(out: OutputStream, len: Int)` to fill specified `OutputStream`;
 - `done(out: ByteBuffer)` to fill specified `ByteBuffer`;
 - `done()` that returns a single byte hash value;
 - `doneShort()`, `doneInt()` and `doneLong()` that returns a single short, int or long hash value;
 - `doneBigInt(bitLength: Int)` that returns positive BigInt with specified length in bits;
 - `doneHex(resultLength: Int)` that returns hex encoded string with specified output length in characters;
 - `doneBaseXXX(len: Int)` that returns string representative of XXX encoded as it defined in [RFC 4648] without padding;
 - `doneXor(...)` that applied hash to existed value via XOR;
 - `doneCallBack(..)` and `doneXorCallBack(...)` which is used callback to for each produced byte.
 
This implementation is thread-safe and you can use it in multithreaded environment.
Anyway this implementation doesn't currently include any multithreading optimizations.

As baseline for benchmarks I've used original C version [c-0.3.7] via JNI interface
 that was implemented as part of [BLAKE3jni].

All benchmarks was performed on two machines:
- `Zulu16+59-CA (build 16-ea+24)` at [Intel® Core™ i7-8700B] with [AVX2 assembly] optimization inside baseline,
- `Zulu16+65-CA (build 16-ea+24)` at [Apple M1] without any assembly optimization inside baseline.

Short summary:
 - it is about 5 times slower than [AVX2 assembly] version via JNI that is expected,
 - it is about 30% slower than original C version via JNI,
 - it has memory footprint near 20% of hashed data that is cleaned up by GC,
 - it has near to constant memory footprint that won't be cleaned up by GC,
 - increasing result hash size has the same impact such as hashing.

Full version of results are available as
 - for [Intel® Core™ i7-8700B] at [jmh-result.intel.json] or via [Intel @ JMH Visualizer].
 - for [Apple M1] at [jmh-result.m1.json] or via [M1 @ JMH Visualizer].

[SHA]: https://github.com/catap/scala-sha
[maven-central]: https://img.shields.io/maven-central/v/pt.kcry/blake3_2.13?style=flat-square
[RFC 4648]: https://tools.ietf.org/html/rfc4648
[BLAKE3jni]: https://github.com/sken77/BLAKE3jni
[AVX2 assembly]: https://github.com/BLAKE3-team/BLAKE3/blob/master/c/blake3_avx2_x86-64_unix.S
[c-0.3.7]: https://github.com/BLAKE3-team/BLAKE3/releases/tag/c-0.3.7
[Intel® Core™ i7-8700B]: https://ark.intel.com/content/www/us/en/ark/products/134905/intel-core-i7-8700b-processor-12m-cache-up-to-4-60-ghz.html
[Apple M1]: https://www.apple.com/mac/m1/
[jmh-result.intel.json]: jmh-result.intel.json
[jmh-result.m1.json]: jmh-result.m1.json
[Intel @ JMH Visualizer]: https://jmh.morethan.io/?source=https://raw.githubusercontent.com/kcrypt/scala-blake3/master/jmh-result.intel.json
[M1 @ JMH Visualizer]: https://jmh.morethan.io/?source=https://raw.githubusercontent.com/kcrypt/scala-blake3/master/jmh-result.m1.json
