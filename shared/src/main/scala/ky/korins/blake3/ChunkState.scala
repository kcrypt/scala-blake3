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

  private def roundBlock(blockWords: Array[Int]): Unit = {
    var off = 0
    var i = 0
    while (off < blockLen) {
      blockLen - off match {
        case 1 =>
          blockWords(i) = block(off) & 0xff

        case 2 =>
          blockWords(i) = ((block(off + 1) & 0xff) << 8) |
            (block(off) & 0xff)

        case 3 =>
          blockWords(i) = ((block(off + 2) & 0xff) << 16) |
            ((block(off + 1) & 0xff) << 8) |
            (block(off) & 0xff)

        case _ =>
          blockWords(i) = ((block(off + 3) & 0xff) << 24) |
            ((block(off + 2) & 0xff) << 16) |
            ((block(off + 1) & 0xff) << 8) |
            (block(off) & 0xff)
      }

      off += 4
      i += 1
    }
    while (i < BLOCK_LEN_WORDS) {
      blockWords(i) = 0
      i += 1
    }
  }

  def unsafeOutput(): Output = {
    roundBlock(tmpBlockWords)
    new Output(
      chainingValue,
      tmpBlockWords,
      chunkCounter,
      blockLen,
      flags | startFlag() | CHUNK_END
    )
  }

  def output(): Output = {
    val safeChainingValue = new Array[Int](KEY_LEN_WORDS)
    System.arraycopy(chainingValue, 0, safeChainingValue, 0, KEY_LEN_WORDS)

    val safeBlockWords = new Array[Int](BLOCK_LEN_WORDS)
    roundBlock(safeBlockWords)

    new Output(
      safeChainingValue,
      safeBlockWords,
      chunkCounter,
      blockLen,
      flags | startFlag() | CHUNK_END
    )
  }
}
