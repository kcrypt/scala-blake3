# Blake3 for scala

This is blake3 implementation for scala-2.11, scala-2.12, scala-2.13, scala-3.0.0-M1, scala-js-1.0 and scala-native-0.4.0-M2.

You can use it as
```
libraryDependencies += "ky.korins" %%% "blake3" % "x.x.x"
```
The latest version is ![maven-central](https://img.shields.io/maven-central/v/ky.korins/blake3_2.13?style=flat-square)

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

Performance on `AdoptOpenJDK (build 11.0.9.1+1)` at `Intel® Core™ i7-8700B`:
```
Benchmark             (dataLen)  (hashLen)  Mode  Cnt   Score   Error  Units
Blake3Benchmark.hash          0         32  avgt    5   0,430 ± 0,012  us/op
Blake3Benchmark.hash          0         64  avgt    5   0,430 ± 0,006  us/op
Blake3Benchmark.hash          0        128  avgt    5   0,667 ± 0,010  us/op
Blake3Benchmark.hash          0        256  avgt    5   1,188 ± 0,017  us/op
Blake3Benchmark.hash         64         32  avgt    5   0,429 ± 0,001  us/op
Blake3Benchmark.hash         64         64  avgt    5   0,425 ± 0,005  us/op
Blake3Benchmark.hash         64        128  avgt    5   0,660 ± 0,024  us/op
Blake3Benchmark.hash         64        256  avgt    5   1,215 ± 0,015  us/op
Blake3Benchmark.hash        256         32  avgt    5   1,070 ± 0,002  us/op
Blake3Benchmark.hash        256         64  avgt    5   1,085 ± 0,028  us/op
Blake3Benchmark.hash        256        128  avgt    5   1,306 ± 0,004  us/op
Blake3Benchmark.hash        256        256  avgt    5   1,819 ± 0,014  us/op
Blake3Benchmark.hash       1024         32  avgt    5   3,592 ± 0,007  us/op
Blake3Benchmark.hash       1024         64  avgt    5   3,623 ± 0,009  us/op
Blake3Benchmark.hash       1024        128  avgt    5   3,869 ± 0,017  us/op
Blake3Benchmark.hash       1024        256  avgt    5   4,401 ± 0,022  us/op
Blake3Benchmark.hash      16384         32  avgt    5  58,235 ± 1,292  us/op
Blake3Benchmark.hash      16384         64  avgt    5  58,502 ± 0,259  us/op
Blake3Benchmark.hash      16384        128  avgt    5  58,808 ± 0,261  us/op
Blake3Benchmark.hash      16384        256  avgt    5  59,770 ± 1,406  us/op
```
