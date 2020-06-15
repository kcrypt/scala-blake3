#!/bin/sh -e

sbt clean test ++2.11.12 blake3/publishLocal ++2.12.11 blake3/publishLocal ++2.13.2 blake3/publishLocal
