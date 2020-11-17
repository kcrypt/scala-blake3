package ky.korins

package object blake3 {
  // constants
  val OUT_LEN: Int = 32
  val KEY_LEN: Int = 32
  val BLOCK_LEN: Int = 64
  val CHUNK_LEN: Int = 1024

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

  private[blake3] val MSG_PERMUTATION = Array(
    2, 6, 3, 10, 7, 0, 4, 13, 1, 11, 12, 5, 9, 14, 15, 8
  )
}
