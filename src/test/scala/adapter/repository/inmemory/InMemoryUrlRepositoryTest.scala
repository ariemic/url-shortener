package adapter.repository.inmemory

import munit.FunSuite
import domain.models.{MappedUrl, OriginalUrl, ShortCode}
import domain.UrlRepository

class InMemoryUrlRepositoryTest extends FunSuite {

  // Helper to create a fresh repository for each test
  def createRepository(): UrlRepository = new InMemoryUrlRepository()

  test("save and findByShortCode - should find saved URL") {
    val repo = createRepository()

    // Arrange - create test data
    val shortCode = ShortCode("abc123")
    val originalUrl = OriginalUrl("https://google.com")
    val mappedUrl = MappedUrl(shortCode, originalUrl)

    // Act - save it
    repo.save(mappedUrl)

    // Assert - find by short code
    val result = repo.findByShortCode(shortCode)

    assertEquals(result, Some(mappedUrl))

  }

  test("findByShortCode - should return None when code doesn't exist") {
    val repo = createRepository()

    val result = repo.findByShortCode(ShortCode("nonexistent"))

    assertEquals(result, None)

  }

  test("findByOriginalUrl - should find URL even with different object instance") {
    //given
    val repo = createRepository()
    val shortCode = ShortCode("abc123")
    val originalUrl = OriginalUrl("https://google.com")
    val mappedUrl = MappedUrl(shortCode, originalUrl)
    repo.save(mappedUrl)

    //when
    val result = repo.findByOriginalUrl(originalUrl)

    //then
    assertEquals(result, Some(mappedUrl))

  }

  test("findByOriginalUrl - should return None when URL doesn't exist") {
    val repo = createRepository()

    // Act
    val result = repo.findByOriginalUrl(OriginalUrl("https://notfound.com"))
    assertEquals(result, None)
  }

  test("save multiple URLs - should find each one independently") {
    val repo = createRepository()

    // Arrange - create multiple mappings
    val mapping1 = MappedUrl(ShortCode("code1"), OriginalUrl("https://first.com"))
    val mapping2 = MappedUrl(ShortCode("code2"), OriginalUrl("https://second.com"))
    val mapping3 = MappedUrl(ShortCode("code3"), OriginalUrl("https://third.com"))

    // Act - save all
    repo.save(mapping1)
    repo.save(mapping2)
    repo.save(mapping3)

    // Assert - can find each one by short code
    val result1 = repo.findByShortCode(mapping1.shortCode)
    val result2 = repo.findByShortCode(mapping2.shortCode)
    val result3 = repo.findByShortCode(mapping3.shortCode)

    assertEquals(result1, Some(mapping1))
    assertEquals(result2, Some(mapping2))
    assertEquals(result3, Some(mapping3))
  }

  test("save multiple URLs - should find each by original URL") {
    val repo = createRepository()

    // Arrange
    val mapping1 = MappedUrl(ShortCode("aaa"), OriginalUrl("https://url1.com"))
    val mapping2 = MappedUrl(ShortCode("bbb"), OriginalUrl("https://url2.com"))

    // Act
    repo.save(mapping1)
    repo.save(mapping2)

    // Assert
    val result1 = repo.findByOriginalUrl(mapping1.originalUrl)
    val result2 = repo.findByOriginalUrl(mapping2.originalUrl)

    assertEquals(result1, Some(mapping1))
    assertEquals(result2, Some(mapping2))

  }
}
