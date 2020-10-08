#!/bin/sh -e

sbt clean test \
  ++2.11.12 publishLocal \
  ++2.12.11 publishLocal \
  ++2.13.3 publishLocal \
  ++0.27.0-RC1 publishLocal
