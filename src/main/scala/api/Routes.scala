package api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import domain.UrlMapper
import domain.models.OriginalUrl
import api.JsonProtocol._

class Routes(urlMapper: UrlMapper) {

  val routes: Route = {
    pathPrefix("api") {
      shortenRoute ~ redirectRoute
    }
  }

  // POST /api/shorten
  // Body: { "url": "https://google.com" }
  // Response: { "shortCode": "abc123", "originalUrl": "https://google.com", "shortUrl": "http://localhost:8080/abc123" }
  private val shortenRoute: Route = {
    path("shorten") {
      post {
        entity(as[ShortenRequest]) { request =>
          val originalUrl: OriginalUrl = OriginalUrl(request.url)
          val result = urlMapper.shorten(originalUrl)
          result match {
            case Right(mappedUrl) =>
              complete(StatusCodes.OK, toShortenResponse(mappedUrl))
            case Left(error) =>
              complete(StatusCodes.BadRequest, ErrorResponse(error.toString))
          }
        }
      }
    }
  }

  // GET /{shortCode}
  // Redirects to the original URL
  private val redirectRoute: Route = {
    path(Segment) { shortCode =>
      get {
        // TODO:
        // 1. Look up the original URL using urlMapper or repository
        
        // 2. If found, redirect (301 or 302)
        // 3. If not found, return 404

        // Hint: You might need to inject repository into Routes
        // Hint: Use redirect(originalUrl, StatusCodes.PermanentRedirect)
        

        complete(StatusCodes.NotImplemented, ErrorResponse("Redirect not implemented yet"))
      }
    }
  }
}
