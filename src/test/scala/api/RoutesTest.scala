package api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server.Route
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json._
import api.JsonProtocol._

// Domain imports
import domain.{UrlFacade, UrlMapper}
import adapter.generator.RandomShortCodeGenerator
import adapter.repository.inmemory.InMemoryUrlRepository

class RoutesTest extends AnyWordSpec with Matchers with ScalatestRouteTest {

  // Helper to create fresh routes with dependencies
  def createRoutes(): Route = {
    val generator = new RandomShortCodeGenerator()
    val repository = new InMemoryUrlRepository()
    val urlMapper = new UrlMapper(generator, repository)
    val urlFacade = new UrlFacade(urlMapper, repository)
    new Routes(urlFacade).routes
  }

  "The URL Shortener API" should {

    "return a shortened URL for POST /api/shorten" in {
      val routes = createRoutes()

      val requestJson = """{"url": "https://google.com"}"""
      val requestEntity = HttpEntity(ContentTypes.`application/json`, requestJson)

      Post("/api/shorten", requestEntity) ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldBe ContentTypes.`application/json`

        val response = responseAs[String].parseJson.convertTo[ShortenResponse]
        response.originalUrl shouldEqual "https://google.com"
        response.shortCode.length shouldEqual 6
        response.shortUrl should include(response.shortCode)
      }
    }

    "return the same short code for duplicate URLs" in {
      val routes = createRoutes()

      val requestJson = """{"url": "https://example.com"}"""
      val requestEntity = HttpEntity(ContentTypes.`application/json`, requestJson)

      // First request
      var firstShortCode = ""
      Post("/api/shorten", requestEntity) ~> routes ~> check {
        val response = responseAs[String].parseJson.convertTo[ShortenResponse]
        firstShortCode = response.shortCode
      }

      // Second request with same URL
      Post("/api/shorten", requestEntity) ~> routes ~> check {
        val response = responseAs[String].parseJson.convertTo[ShortenResponse]
        response.shortCode shouldEqual firstShortCode
      }
    }

    "redirect to original URL for GET /{shortCode}" in {
      val routes = createRoutes()

      // First, create a short URL
      val requestJson = """{"url": "https://google.com"}"""
      val requestEntity = HttpEntity(ContentTypes.`application/json`, requestJson)

      var shortCode = ""
      Post("/api/shorten", requestEntity) ~> routes ~> check {
        val response = responseAs[String].parseJson.convertTo[ShortenResponse]
        shortCode = response.shortCode
      }

      // Now test the redirect
      Get(s"/$shortCode") ~> routes ~> check {
        status shouldEqual StatusCodes.PermanentRedirect
        header("Location").map(_.value()) shouldEqual Some("https://google.com")
      }
    }

    "return 404 for non-existent short code" in {
      val routes = createRoutes()

      Get("/nonexistent") ~> routes ~> check {
        status shouldEqual StatusCodes.NotFound
        contentType shouldBe ContentTypes.`application/json`

        val response = responseAs[String].parseJson.convertTo[ErrorResponse]
        response.message should include("not found")
      }
    }

    "handle multiple different URLs" in {
      val routes = createRoutes()

      val url1Json = """{"url": "https://first.com"}"""
      val url2Json = """{"url": "https://second.com"}"""

      var shortCode1 = ""
      var shortCode2 = ""

      // Create first short URL
      Post("/api/shorten", HttpEntity(ContentTypes.`application/json`, url1Json)) ~> routes ~> check {
        val response = responseAs[String].parseJson.convertTo[ShortenResponse]
        shortCode1 = response.shortCode
      }

      // Create second short URL
      Post("/api/shorten", HttpEntity(ContentTypes.`application/json`, url2Json)) ~> routes ~> check {
        val response = responseAs[String].parseJson.convertTo[ShortenResponse]
        shortCode2 = response.shortCode
      }

      // Short codes should be different
      shortCode1 should not equal shortCode2

      // Both should redirect correctly
      Get(s"/$shortCode1") ~> routes ~> check {
        header("Location").map(_.value()) shouldEqual Some("https://first.com")
      }

      Get(s"/$shortCode2") ~> routes ~> check {
        header("Location").map(_.value()) shouldEqual Some("https://second.com")
      }
    }
  }
}
