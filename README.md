# Blake3 for scala

This is blake3 implementation for scala-2.11, scala-2.12, scala-2.13, dotty-0.27-RC1, scala-js-1.0 and scala-native-0.4.0-M2.

You can use it as
```
libraryDependencies += "ky.korins" %%% "blake3" % "1.6.1"
```

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
 - `doneBaseXXX(len: Int)` that returns string representative of XXX encoded as it defined in RFC 4648 without padding.
 
This implementation is thread-safe and you can use it in multithreaded environment.
Anyway this implementation doesn't currently include any multithreading optimizations.

Performance on `Intel® Core™ i9-8950HK`:
```
[info] Blake3Benchmark.hash          0         32  avgt    5    19.112 ±   0.873  us/op
[info] Blake3Benchmark.hash          0         64  avgt    5    22.965 ±   0.284  us/op
[info] Blake3Benchmark.hash          0        128  avgt    5    30.977 ±   0.726  us/op
[info] Blake3Benchmark.hash          0        256  avgt    5    48.661 ±   0.982  us/op
[info] Blake3Benchmark.hash         64         32  avgt    5    19.080 ±   0.608  us/op
[info] Blake3Benchmark.hash         64         64  avgt    5    22.675 ±   1.305  us/op
[info] Blake3Benchmark.hash         64        128  avgt    5    31.982 ±   0.202  us/op
[info] Blake3Benchmark.hash         64        256  avgt    5    50.842 ±  17.778  us/op
[info] Blake3Benchmark.hash        256         32  avgt    5    41.505 ±  14.989  us/op
[info] Blake3Benchmark.hash        256         64  avgt    5    42.371 ±  21.145  us/op
[info] Blake3Benchmark.hash        256        128  avgt    5    48.634 ±   3.520  us/op
[info] Blake3Benchmark.hash        256        256  avgt    5    66.164 ±   4.235  us/op
[info] Blake3Benchmark.hash       1024         32  avgt    5    95.868 ±   3.424  us/op
[info] Blake3Benchmark.hash       1024         64  avgt    5   115.464 ±   5.200  us/op
[info] Blake3Benchmark.hash       1024        128  avgt    5   123.400 ±   6.153  us/op
[info] Blake3Benchmark.hash       1024        256  avgt    5   120.792 ±   3.556  us/op
[info] Blake3Benchmark.hash      16384         32  avgt    5  1574.141 ±  28.947  us/op
[info] Blake3Benchmark.hash      16384         64  avgt    5  1518.879 ± 113.151  us/op
[info] Blake3Benchmark.hash      16384        128  avgt    5  1580.558 ±  55.243  us/op
[info] Blake3Benchmark.hash      16384        256  avgt    5  1610.715 ± 101.767  us/op
```
