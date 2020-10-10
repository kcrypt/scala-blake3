#!/bin/sh -e

sbt +clean +test \
  +publishSigned \
  sonatypeBundleRelease
