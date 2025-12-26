package api

import spray.json._
import domain.models.{MappedUrl, OriginalUrl, ShortCode}


case class ShortenRequest(url: String)
case class ShortenResponse(shortCode: String, originalUrl: String, shortUrl: String)
case class ErrorResponse(message: String)

object JsonProtocol extends DefaultJsonProtocol {

  // Define JSON formats for your case classes
  implicit val shortenRequestFormat: RootJsonFormat[ShortenRequest] = jsonFormat1(ShortenRequest.apply)
  implicit val shortenResponseFormat: RootJsonFormat[ShortenResponse] = jsonFormat3(ShortenResponse.apply)
  implicit val errorResponseFormat: RootJsonFormat[ErrorResponse] = jsonFormat1(ErrorResponse.apply)

  // Helper to convert domain model to response DTO
  def toShortenResponse(mappedUrl: MappedUrl, baseUrl: String = "http://localhost:8080"): ShortenResponse = {
    ShortenResponse(
      shortCode = mappedUrl.shortCode.value,
      originalUrl = mappedUrl.originalUrl.value,
      shortUrl = s"$baseUrl/${mappedUrl.shortCode.value}"
    )
  }
}