package ky.korins

package object blake3 {
  // constants
  val OUT_LEN: Int = 32
  val KEY_LEN: Int = 32
  val KEY_LEN_WORDS: Int = 8
  val BLOCK_LEN: Int = 64
  val BLOCK_LEN_WORDS: Int = 16
  val CHUNK_LEN: Int = 1024
  val MAX_DEPTH: Int = 54

  private[blake3] val CHUNK_START = 1 << 0
  private[blake3] val CHUNK_END = 1 << 1
  private[blake3] val PARENT = 1 << 2
  private[blake3] val ROOT = 1 << 3
  private[blake3] val KEYED_HASH = 1 << 4
  private[blake3] val DERIVE_KEY_CONTEXT = 1 << 5
  private[blake3] val DERIVE_KEY_MATERIAL = 1 << 6

  private[blake3] val IV = Array(
    0x6A09E667,
    0xBB67AE85,
    0x3C6EF372,
    0xA54FF53A,
    0x510E527F,
    0x9B05688C,
    0x1F83D9AB,
    0x5BE0CD19
  )
}
