#!/bin/sh -e

sbt clean test \
  ++2.11.12 publishSigned \
  ++2.12.11 publishSigned \
  ++2.13.3 publishSigned \
  ++0.27.0-RC1 publishSigned \
  sonatypeBundleRelease
