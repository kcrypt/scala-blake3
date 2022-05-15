/*
 * scala-blake3 - highly optimized blake3 implementation for scala, scala-js and scala-native.
 *
 * Written in 2020, 2021 by Kirill A. Korinsky <kirill@korins.ky>
 *
 * Supported since 2022 by Kcrypt Lab UG <opensource@kcry.pt>
 *
 * This work is released into the public domain with CC0 1.0.
 * Alternatively, it is licensed under the Apache License 2.0.
 */

package pt.kcry.blake3

import Compress._
import CompressBytesAsBlockWords._

private[blake3] object ChunkState {
  val zerosBlockWords = new Array[Int](BLOCK_LEN_WORDS)
}

private[blake3] class ChunkState(
  val key: Array[Int], var chunkCounter: Long, val flags: Int
) {

  val chainingValue: Array[Int] = new Array[Int](BLOCK_LEN_WORDS)
  System.arraycopy(key, 0, chainingValue, 0, KEY_LEN_WORDS)

  val block: Array[Byte] = new Array[Byte](BLOCK_LEN)

  var blockLen: Int = 0
  var compressedBlocksLen: Int = 0

  private val tmpBlockWords: Array[Int] = new Array[Int](BLOCK_LEN_WORDS)

  def reset(key: Array[Int]): Long = {
    System.arraycopy(key, 0, chainingValue, 0, KEY_LEN_WORDS)
    chunkCounter += 1
    blockLen = 0
    compressedBlocksLen = 0

    chunkCounter // aka totalChunks
  }

  def len(): Int = compressedBlocksLen + blockLen

  private def startFlag(): Int =
    if (compressedBlocksLen == 0) CHUNK_START else 0

  private def compressedWords(bytes: Array[Byte], bytesOffset: Int): Unit = {
    compressBytesAsBlockWords(bytes, bytesOffset, tmpBlockWords)
    compressInPlace(chainingValue, chainingValue, tmpBlockWords, chunkCounter,
      BLOCK_LEN, flags | startFlag())
    compressedBlocksLen += BLOCK_LEN
    blockLen = 0
  }

  private def compressIfRequired(): Unit =
    if (blockLen == BLOCK_LEN) compressedWords(block, 0)

  def update(bytes: Array[Byte], from: Int, to: Int): Unit = {
    var i = from

    val available = BLOCK_LEN - blockLen
    var consume = Math.min(available, to - i)
    if (consume > 0) {
      System.arraycopy(bytes, i, block, blockLen, consume)
      blockLen += consume
      i += consume
    }

    consume = to - i
    if (consume > 0) compressIfRequired()

    while (consume > BLOCK_LEN) {
      blockLen = BLOCK_LEN
      compressedWords(bytes, i)
      i += BLOCK_LEN
      consume = to - i
    }

    if (consume > 0) {
      System.arraycopy(bytes, i, block, blockLen, consume)
      blockLen += consume
    }
  }

  def update(byte: Byte): Unit = {
    compressIfRequired()
    block(blockLen) = byte
    blockLen += 1
  }

  private def roundBlock(blockWords: Array[Int]): Unit = {
    var off = 0
    var i = 0
    while (off < blockLen) {
      blockLen - off match {
        case 1 => blockWords(i) = block(off) & 0xff

        case 2 => blockWords(i) = ((block(off + 1) & 0xff) << 8) |
            (block(off) & 0xff)

        case 3 => blockWords(i) = ((block(off + 2) & 0xff) << 16) |
            ((block(off + 1) & 0xff) << 8) | (block(off) & 0xff)

        case _ => blockWords(i) = ((block(off + 3) & 0xff) << 24) |
            ((block(off + 2) & 0xff) << 16) | ((block(off + 1) & 0xff) << 8) |
            (block(off) & 0xff)
      }

      off += 4
      i += 1
    }

    val zeros = BLOCK_LEN_WORDS - i
    if (zeros > 0) System.arraycopy(ChunkState.zerosBlockWords, 0, blockWords,
      i, zeros)
  }

  def unsafeOutput(): Output = {
    roundBlock(tmpBlockWords)
    new Output(chainingValue, tmpBlockWords, chunkCounter, blockLen,
      flags | startFlag() | CHUNK_END)
  }

  def output(): Output = {
    val safeChainingValue = new Array[Int](KEY_LEN_WORDS)
    System.arraycopy(chainingValue, 0, safeChainingValue, 0, KEY_LEN_WORDS)

    val safeBlockWords = new Array[Int](BLOCK_LEN_WORDS)
    roundBlock(safeBlockWords)

    new Output(safeChainingValue, safeBlockWords, chunkCounter, blockLen,
      flags | startFlag() | CHUNK_END)
  }
}
