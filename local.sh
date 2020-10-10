#!/bin/sh -e

sbt +clean \
  +test \
  +publishLocal
