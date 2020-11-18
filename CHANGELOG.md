# Changelog

All notable changes to this project will be documented in this file.

## [unreleased]

## [1.8.0] - 2020-11-18
- Reduce memory footprint when compress is required.
- Enabled `BuildInfo` plugin.

## [1.7.1] - 2020-11-18
- Fixed an infinity loop at `done` when `off` is bigger than `CHUNK_LEN`.

## [1.7.0] - 2020-11-18
- Significant performance improvement and reducing memory footprint.
- Fixed `BuildInfo` by moving it to `ky.korins.blake3` package.

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

[unreleased]: https://github.com/catap/scala-blake3/compare/v1.8.0...HEAD
[1.8.0]: https://github.com/catap/scala-blake3/compare/v1.7.1...v1.8.0
[1.7.1]: https://github.com/catap/scala-blake3/compare/v1.7.0...v1.7.1
[1.7.0]: https://github.com/catap/scala-blake3/compare/v1.6.2...v1.7.0
[1.6.2]: https://github.com/catap/scala-blake3/compare/v1.6.1...v1.6.2
[1.6.1]: https://github.com/catap/scala-blake3/compare/v1.6.0...v1.6.1
[1.6.0]: https://github.com/catap/scala-blake3/compare/v1.5.0...v1.6.0
[1.5.0]: https://github.com/catap/scala-blake3/compare/v1.4.0...v1.5.0
[1.4.0]: https://github.com/catap/scala-blake3/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/catap/scala-blake3/compare/v1.2.0...v1.3.0
[1.2.0]: https://github.com/catap/scala-blake3/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/catap/scala-blake3/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/catap/scala-blake3/releases/tag/v1.0.0
