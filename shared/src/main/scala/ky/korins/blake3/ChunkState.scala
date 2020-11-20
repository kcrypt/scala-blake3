package ky.korins.blake3

import CommonFunction._

private[blake3] class ChunkState(
  val key: Array[Int],
  var chunkCounter: Long,
  val flags: Int
) {

  val chainingValue: Array[Int] = new Array[Int](BLOCK_LEN_WORDS)
  System.arraycopy(key, 0, chainingValue, 0, KEY_LEN_WORDS)

  val block: Array[Byte] = new Array[Byte](BLOCK_LEN)

  var blockLen: Int = 0
  var blocksCompressed: Int = 0

  private val tmpBlockWords: Array[Int] = new Array[Int](BLOCK_LEN_WORDS)
  private val tmpState = new Array[Int](BLOCK_LEN_WORDS)

  def reset(key: Array[Int]): Long = {
    System.arraycopy(key, 0, chainingValue, 0, KEY_LEN_WORDS)
    chunkCounter += 1
    blockLen = 0
    blocksCompressed = 0

    chunkCounter // aka totalChunks
  }

  def len(): Int =
    BLOCK_LEN * blocksCompressed + blockLen

  private def startFlag(): Int =
    if (blocksCompressed == 0)
      CHUNK_START
    else 0

  private def compressedWords(bytes: Array[Byte], bytesOffset: Int): Unit = {
    compressInPlace(
      chainingValue,
      bytes,
      bytesOffset,
      chunkCounter,
      BLOCK_LEN,
      flags | startFlag(),
      tmpState,
      tmpBlockWords
    )
    blocksCompressed += 1
    blockLen = 0
  }

  private def compressIfRequired(): Unit =
    if (blockLen == BLOCK_LEN) {
      compressedWords(block, 0)
    }

  def update(bytes: Array[Byte], from: Int, to: Int): Unit = {
    var i = from

    var consume = Math.min(BLOCK_LEN - blockLen, to - i)
    if (consume < BLOCK_LEN) {
      System.arraycopy(bytes, i, block, blockLen, consume)
      blockLen += consume
      i += consume
      compressIfRequired()
    }

    consume = to - i
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

  def output(): Output = {
    var i = blockLen
    while (i < BLOCK_LEN) {
      block(i) = 0
      i += 1
    }

    val safeChainingValue = new Array[Int](KEY_LEN_WORDS)
    System.arraycopy(chainingValue, 0, safeChainingValue, 0, KEY_LEN_WORDS)

    new Output(safeChainingValue, wordsFromLittleEndianBytes(block),
      chunkCounter, blockLen, flags | startFlag() | CHUNK_END)
  }
}
