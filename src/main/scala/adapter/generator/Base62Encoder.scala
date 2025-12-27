package adapter.generator

import scala.annotation.tailrec


object Base62Encoder {

  private val BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val BASE = 62
  
  @tailrec
  def encode(number: Long, accumulator: String = ""): String = {
    if (number == 0) {
      if (accumulator.isEmpty) "0" else accumulator
    } else {
      val remainder = (number % BASE).toInt
      val char = BASE62_CHARS(remainder)
      encode(number / BASE, char + accumulator)
    }
  }
  
  def decode(encoded: String): Long = {
    encoded.foldLeft(0L) { (acc, char) =>
      val value = BASE62_CHARS.indexOf(char)
      if (value == -1) throw new IllegalArgumentException(s"Invalid Base62 character: $char")
      acc * BASE + value
    }
  }
}
