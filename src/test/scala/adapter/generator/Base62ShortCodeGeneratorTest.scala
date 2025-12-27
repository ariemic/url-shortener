package adapter.generator

import munit.FunSuite

class Base62ShortCodeGeneratorTest extends FunSuite {

  test("should generate shortCode using base62 algorithm") {
    //given
    val generator: Base62ShortCodeGenerator = Base62ShortCodeGenerator()
    //when
    val code = generator.generate()
    assert(code.value.length >= 1)
    assert(code.value.forall(c =>
      c.isDigit || c.isLower || c.isUpper
    ))

  }

  test("generate should create sequential codes") {
    val generator = new Base62ShortCodeGenerator()

    val code1 = generator.generate()
    val code2 = generator.generate()
    val code3 = generator.generate()

    // Should be different (counter increments)
    assert(code1.value != code2.value)
    assert(code2.value != code3.value)
  }


  test("counter starts at 1 and increments") {
    val generator = new Base62ShortCodeGenerator()

    val first = generator.generate()
    // First code should be "1" (encoding of 1)
    assertEquals(first.value, "1")

    val second = generator.generate()
    assertEquals(second.value, "2")
  }

}