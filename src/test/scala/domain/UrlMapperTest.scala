package domain

import munit.FunSuite
import domain.models.{MappedUrl, OriginalUrl, ShortCode}
import adapter.repository.inmemory.InMemoryUrlRepository
import adapter.generator.RandomShortCodeGenerator

class UrlMapperTest extends FunSuite {

  // Helper to create mapper with dependencies
  def createMapper(): UrlMapper = {
    val generator = new RandomShortCodeGenerator()
    val repository = new InMemoryUrlRepository()
    new UrlMapper(generator, repository)
  }

  test("shorten - should generate short code that has length 6 and return MappedUrl") {
    val mapper = createMapper()
    val originalUrl = OriginalUrl("https://google.com")

    // Act
    val result = mapper.shorten(originalUrl)

    // Assert
    assert(result.isRight)
    result match {
      case Right(mappedUrl) =>
        assertEquals(mappedUrl.originalUrl, originalUrl)
        assert(mappedUrl.shortCode.value.length == 6)
      case Left(error) =>
        fail(s"Expected Right but got Left: $error")
    }
  }

  test("shorten - should return existing mapping if URL already shortened") {
    val mapper = createMapper()
    val originalUrl = OriginalUrl("https://example.com")

    // Act - shorten the same URL twice
    val result1 = mapper.shorten(originalUrl)
    val result2 = mapper.shorten(originalUrl)

    // Assert
    assert(result1.isRight)
    assert(result2.isRight)

    assert(result1.map(_.shortCode) == result2.map(_.shortCode))
  }

  test("shorten - different URLs should get different short codes") {
    val mapper = createMapper()
    val url1 = OriginalUrl("https://first.com")
    val url2 = OriginalUrl("https://second.com")

    // Act
    val result1 = mapper.shorten(url1)
    val result2 = mapper.shorten(url2)

    // Assert
    assert(result1.isRight)
    assert(result2.isRight)

    assert(result1.map(_.shortCode) != result2.map(_.shortCode))
  }
}
