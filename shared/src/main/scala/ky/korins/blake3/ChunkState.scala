package ky.korins.blake3

import CommonFunction._

private[blake3] class ChunkState(
  var chainingValue: Vector[Int],
  val chunkCounter: Long,
  val block: Array[Byte],
  var blockLen: Int,
  var blocksCompressed: Int,
  val flags: Int
) {
  def this(key: Vector[Int], chunkCounter: Long, flags: Int) =
    this(key, chunkCounter, Array.fill[Byte](BLOCK_LEN)(0), 0, 0, flags)

  def len(): Int =
    BLOCK_LEN * blocksCompressed.toInt + blockLen.toInt

  private def startFlag(): Int =
    if (blocksCompressed == 0)
      CHUNK_START
    else 0

  private def compressIfRequired(): Unit = {
    if (blockLen == BLOCK_LEN) {
      chainingValue = first8Words(compress(
        chainingValue,
        wordsFromLittleEndianBytes(block),
        chunkCounter,
        BLOCK_LEN,
        flags | startFlag()
      ))
      blocksCompressed += 1
      var i = 0
      while (i < block.length) {
        block(i) = 0
        i += 1
      }
      blockLen = 0
    }
  }

  def update(bytes: Array[Byte], from: Int, to: Int): Unit = {
    var i = from
    while (i < to) {
      compressIfRequired()
      val consume = Math.min(BLOCK_LEN - blockLen, to - i)
      Array.copy(bytes, i, block, blockLen, consume)
      blockLen += consume
      i += consume
    }
  }

  def update(byte: Byte): Unit = {
    compressIfRequired()
    block(blockLen) = byte
    blockLen += 1
  }

  def output(): Output =
    new Output(chainingValue, wordsFromLittleEndianBytes(block),
      chunkCounter, blockLen.toInt, flags | startFlag() | CHUNK_END)
}
