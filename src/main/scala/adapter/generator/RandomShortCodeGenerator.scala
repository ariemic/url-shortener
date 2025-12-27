package adapter.generator

import domain.ShortCodeGenerator
import domain.models.{OriginalUrl, ShortCode}

import scala.util.Random

class RandomShortCodeGenerator extends ShortCodeGenerator {

  private val CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val CODE_LENGTH = 6
  private val random = new Random()

  //is this create new characters one by one and go to end operation or move every to through 1st then 
  //go to 2nd and last ?
  override def generate(url: Option[OriginalUrl] = None): ShortCode = {
    val code = (1 to CODE_LENGTH)
      .map(_ => CHARS(random.nextInt(CHARS.length)))
      .mkString

    ShortCode(code)
  }
}
