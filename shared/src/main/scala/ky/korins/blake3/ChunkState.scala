package ky.korins.blake3

import CommonFunction._

private[blake3] class ChunkState(
  var chainingValue: Array[Int],
  val chunkCounter: Long,
  val block: Array[Byte],
  var blockLen: Int,
  var blocksCompressed: Int,
  val flags: Int
) {
  def this(key: Array[Int], chunkCounter: Long, flags: Int) =
    this(key, chunkCounter, new Array[Byte](BLOCK_LEN), 0, 0, flags)

  def len(): Int =
    BLOCK_LEN * blocksCompressed.toInt + blockLen.toInt

  private def startFlag(): Int =
    if (blocksCompressed == 0)
      CHUNK_START
    else 0

  private def compressIfRequired(): Unit = {
    if (blockLen == BLOCK_LEN) {
      chainingValue = compress(
        chainingValue,
        wordsFromLittleEndianBytes(block),
        chunkCounter,
        BLOCK_LEN,
        flags | startFlag()
      )
      blocksCompressed += 1
      blockLen = 0
    }
  }

  def update(bytes: Array[Byte], from: Int, to: Int): Unit = synchronized {
    var i = from
    while (i < to) {
      compressIfRequired()
      val consume = Math.min(BLOCK_LEN - blockLen, to - i)
      System.arraycopy(bytes, i, block, blockLen, consume)
      blockLen += consume
      i += consume
    }
  }

  def update(byte: Byte): Unit = synchronized {
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
    new Output(chainingValue, wordsFromLittleEndianBytes(block),
      chunkCounter, blockLen.toInt, flags | startFlag() | CHUNK_END)
  }
}
