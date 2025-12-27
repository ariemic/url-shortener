package adapter.generator

import domain.ShortCodeGenerator
import domain.models.{OriginalUrl, ShortCode}

import java.security.MessageDigest


//todo need to handle collision
//we can add a random salt to url before hashing to reduce collision probability
//and catch collision and retry with different salt until success
class HashBasedShortCodeGenerator extends ShortCodeGenerator {

  override def generate(url: Option[OriginalUrl] = None): ShortCode = {
    url match {
      case Some(originalUrl) =>
        val hash = computeHash(originalUrl.value)
        ShortCode(Base62Encoder.encode(hash))
      case None =>
        throw new IllegalArgumentException("Hash-based generator requires URL")
    }
  }

  private def computeHash(url: String): Long = {
    val sha256 = MessageDigest.getInstance("SHA-256")
    val hashBytes = sha256.digest(url.getBytes("UTF-8"))
    bytesToLong(hashBytes.take(6))
  }

  private def bytesToLong(bytes: Array[Byte]): Long = {
    bytes.foldLeft(0L) { (acc, byte) =>
      (acc << 8) | (byte & 0xFF)
    }
  }
}
