package adapter.generator

import domain.ShortCodeGenerator
import domain.models.ShortCode

import scala.util.Random

class RandomShortCodeGenerator extends ShortCodeGenerator{

  private val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  private val length = 6
  private val random = new Random()
  
  //is this create new characters one by one and go to end operation or move every to through 1st then 
  //go to 2nd and last ?
  def generate(): ShortCode = {
    val code = (1 to length)
      .map(_ => chars(random.nextInt(chars.length)))
      .mkString

    ShortCode(code)
  }


}
