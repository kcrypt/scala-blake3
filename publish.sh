#!/bin/sh -e

# To prevent publish the same artifact at scala-native I've used this hack :(
sbt clean test \
  ++2.11.12 blake3Native/publishSigned blake3JS/publishSigned blake3JVM/publishSigned \
  ++2.12.11 blake3JS/publishSigned blake3JVM/publishSigned \
  ++2.13.2 blake3JS/publishSigned blake3JVM/publishSigned
