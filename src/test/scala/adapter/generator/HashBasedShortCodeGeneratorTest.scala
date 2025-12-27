package adapter.generator

import domain.models.OriginalUrl
import munit.FunSuite

class HashBasedShortCodeGeneratorTest extends FunSuite {

  test("should generate shortCode using hash based algorithm") {
    //given
    val url = OriginalUrl("https://google.com")
    val generator: HashBasedShortCodeGenerator = HashBasedShortCodeGenerator()
    //when
    val code = generator.generate(Some(url))

    //then
    assert(code.value.length >= 1)

  }


  test("should generate same code for same URL") {
    //given
    val url = OriginalUrl("https://example.com/some/long/path")
    val generator: HashBasedShortCodeGenerator = HashBasedShortCodeGenerator()

    //when
    val code1 = generator.generate(Some(url))
    val code2 = generator.generate(Some(url))

    //then
    assertEquals(code1, code2)
  }


  test("should generate different codes for different URLs") {
    //given
    val url1 = OriginalUrl("https://example.com/first/path")
    val url2 = OriginalUrl("https://example.com/second/path")
    val generator: HashBasedShortCodeGenerator = HashBasedShortCodeGenerator()

    //when
    val code1 = generator.generate(Some(url1))
    val code2 = generator.generate(Some(url2))

    //then
    assert(code1 != code2)
  }

}
