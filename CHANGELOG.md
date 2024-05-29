# Changelog

All notable changes to this project will be documented in this file.

## [unreleased]
- Switched to scala-2.13.14
- Switched to scala-3.4.2
- Switched to scala-native-0.5.2

## [3.1.2] - 2024-04-24
- Drop support scala-2.12 for Scala.JS and Scala-Native
- Switched to scala-2.12.19
- Switched to scala-2.13.13
- Switched to scala-3.4.1
- Switched to scalajs-1.16.0
- Switched to scala-native-0.5.1

## [3.1.1] - 2022-05-19
- Performance improved: now it's about 20% slower than C version

## [3.1.0] - 2022-05-17
- Introduced `doneCallback` and `doneXorCallBack`
- Achieve constant memory footprint

## [3.0.1] - 2022-05-11
- Switched to scala-3.1.2
- Switched to scalajs-1.10.0
- Switched to scala-native-0.4.4

## [3.0.0] - 2021-12-23
- Migrated to package `pt.kcry.blake3`
- Switched to scalajs-1.8.0
- Switched to scala-native-0.4.2
- Switched to scala-2.13.7

## [2.9.0] - 2021-08-06
- Switched to Scala.js-1.7.0.
- `hasher` can be used now as `OutputStream`.

## [2.8.0] - 2021-07-16
- Introduced `doneXor`.

## [2.7.0] - 2021-07-15
- Switched to Scala.js-1.6.0.
- Switched to scala 3.0.1.
- Performance improved for near of 3%.

## [2.6.1] - 2021-05-18
- Fixed regression on chunked data that was introduced in 2.6.0.

## [2.6.0] - 2021-05-17
- Fixed an issue when chunked data by block len produced incorrect block.

## [2.5.2] - 2021-05-17
- Switch to scala 3.0.0.
- Enable scala.js via to Scala.js-3.0.0.

## [2.5.1] - 2021-05-17
- Switch to Scala.js-1.5.1.
- Switch to scala 2.13.6 and 3.0.0-RC3.

## [2.5.0] - 2021-02-27
- Introduced support direct allocated ByteBuffer.
- Cross build for scala-2.10.7.
- Limited read and write to ByteBuffer.

## [2.4.1] - 2021-02-19
- Fixed regression on RFC4648 encoding introduced in 2.4.0.

## [2.4.0] - 2021-02-19
- Improved performance RFC4648 and introduced support user's alphabet.

## [2.3.1] - 2021-02-18
- Switched to Scala.js-1.5.0.
- Switched to Scala-Native-0.4.0.
- Switched to Scala3-3.0.0-RC1.

## [2.3.0] - 2021-01-06
- Introduced `newDeriveKeyHasher(Hasher => Unit)`.
- Introduced `update(Short)`, `update(Int)` and `update(Long)`.

## [2.2.0] - 2021-01-05
- Introduced `doneShort()`, `doneInt()` and `doneLong()`.

## [2.1.0] - 2021-01-04
- Introduced `doneBigInt(N: BigInt)`.

## [2.0.1] - 2021-01-03
- Migrated to scala `3.0.0-M3`.

## [2.0.0] - 2020-12-03
- Introduced `update(ByteBuffer)`.
- Introduced `done(out: OutputStream, len: Int)`.
- Introduced `done(out: ByteBuffer)`.
- Synchronized license with original blake3 implementation.

## [1.9.0] - 2020-11-21
- Significant performance improvement and reducing memory footprint at `done`.
- Improved synchronization logic.
- Introduced `update(InputStream)`.

## [1.8.1] - 2020-11-19
- Performance improvement that reduce CPU and memory footprint near doubles.

## [1.8.0] - 2020-11-18
- Reduce memory footprint when compress is required.
- Enabled `BuildInfo` plugin.

## [1.7.1] - 2020-11-18
- Fixed an infinity loop at `done` when `off` is bigger than `CHUNK_LEN`.

## [1.7.0] - 2020-11-18
- Significant performance improvement and reducing memory footprint.
- Fixed `BuildInfo` by moving it to `pt.kcry.blake3` package.

## [1.6.2] - 2020-11-16
- Migrated from dotty `0.27.0-RC1` to scala `3.0.0-M1`.

## [1.6.1] - 2020-10-10
- Added `BuildInfo` to easy track which version of blake3 is used.

## [1.6.0] - 2020-10-08
- Avoid `assert()` to make exception as clear as it possibly.
- Enabled cross build for dotty `0.27.0-RC1`.

## [1.5.0] - 2020-09-27
- Added synchronization to make it thread safe.

## [1.4.0] - 2020-09-17
- Introduced RFC 4648 compatibly encoding of hash output.

## [1.3.0] - 2020-09-05
- Introduced string level `hash`, `hex` and `update`.

## [1.2.0] - 2020-07-28
- Introduced a way to use any byte array at DKF.
- Introduced `update(Byte)` and `done(): Byte`.

## [1.1.0] - 2020-07-19
- Extended API by adding `update` and `done` for specified position.

## [1.0.0] - 2020-07-15
- The first public release as dedicated project.

[unreleased]: https://github.com/kcrypt/scala-blake3/compare/v3.1.2...HEAD
[3.1.2]: https://github.com/kcrypt/scala-blake3/compare/v3.1.1...v3.1.2
[3.1.1]: https://github.com/kcrypt/scala-blake3/compare/v3.1.0...v3.1.1
[3.1.0]: https://github.com/kcrypt/scala-blake3/compare/v3.0.1...v3.1.0
[3.0.1]: https://github.com/kcrypt/scala-blake3/compare/v3.0.0...v3.0.1
[3.0.0]: https://github.com/kcrypt/scala-blake3/compare/v2.9.0...v3.0.0
[2.9.0]: https://github.com/kcrypt/scala-blake3/compare/v2.8.0...v2.9.0
[2.8.0]: https://github.com/kcrypt/scala-blake3/compare/v2.7.0...v2.8.0
[2.7.0]: https://github.com/kcrypt/scala-blake3/compare/v2.6.1...v2.7.0
[2.6.1]: https://github.com/kcrypt/scala-blake3/compare/v2.6.0...v2.6.1
[2.6.0]: https://github.com/kcrypt/scala-blake3/compare/v2.5.2...v2.6.0
[2.5.2]: https://github.com/kcrypt/scala-blake3/compare/v2.5.1...v2.5.2
[2.5.1]: https://github.com/kcrypt/scala-blake3/compare/v2.5.0...v2.5.1
[2.5.0]: https://github.com/kcrypt/scala-blake3/compare/v2.4.1...v2.5.0
[2.4.1]: https://github.com/kcrypt/scala-blake3/compare/v2.4.0...v2.4.1
[2.4.0]: https://github.com/kcrypt/scala-blake3/compare/v2.3.1...v2.4.0
[2.3.1]: https://github.com/kcrypt/scala-blake3/compare/v2.3.0...v2.3.1
[2.3.0]: https://github.com/kcrypt/scala-blake3/compare/v2.2.0...v2.3.0
[2.2.0]: https://github.com/kcrypt/scala-blake3/compare/v2.1.0...v2.2.0
[2.1.0]: https://github.com/kcrypt/scala-blake3/compare/v2.0.1...v2.1.0
[2.0.1]: https://github.com/kcrypt/scala-blake3/compare/v2.0.0...v2.0.1
[2.0.0]: https://github.com/kcrypt/scala-blake3/compare/v1.9.0...v2.0.0
[1.9.0]: https://github.com/kcrypt/scala-blake3/compare/v1.8.1...v1.9.0
[1.8.1]: https://github.com/kcrypt/scala-blake3/compare/v1.8.0...v1.8.1
[1.8.0]: https://github.com/kcrypt/scala-blake3/compare/v1.7.1...v1.8.0
[1.7.1]: https://github.com/kcrypt/scala-blake3/compare/v1.7.0...v1.7.1
[1.7.0]: https://github.com/kcrypt/scala-blake3/compare/v1.6.2...v1.7.0
[1.6.2]: https://github.com/kcrypt/scala-blake3/compare/v1.6.1...v1.6.2
[1.6.1]: https://github.com/kcrypt/scala-blake3/compare/v1.6.0...v1.6.1
[1.6.0]: https://github.com/kcrypt/scala-blake3/compare/v1.5.0...v1.6.0
[1.5.0]: https://github.com/kcrypt/scala-blake3/compare/v1.4.0...v1.5.0
[1.4.0]: https://github.com/kcrypt/scala-blake3/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/kcrypt/scala-blake3/compare/v1.2.0...v1.3.0
[1.2.0]: https://github.com/kcrypt/scala-blake3/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/kcrypt/scala-blake3/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/kcrypt/scala-blake3/releases/tag/v1.0.0
