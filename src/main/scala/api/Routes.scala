package api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import api.JsonProtocol.*
import domain.UrlFacade
import domain.models.{OriginalUrl, ShortCode}

class Routes(urlFacade: UrlFacade) {
  
  lazy val routes: Route = shortenRoute ~ redirectRoute
  
  private val shortenRoute: Route = {
    path("api" / "shorten") {
      post {
        entity(as[ShortenRequest]) { request =>
          val originalUrl: OriginalUrl = OriginalUrl(request.url)
          val result = urlFacade.shorten(originalUrl)
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
        val code = ShortCode(shortCode)
        urlFacade.redirect(code) match {
          case Some(originalUrl) =>
            println(s"DEBUG: Redirecting to: '${originalUrl.value}'")
            redirect(Uri(originalUrl.value), StatusCodes.PermanentRedirect)
          case None =>
            println(s"DEBUG: Short code not found: '$shortCode'")
            complete(StatusCodes.NotFound, ErrorResponse("Short code not found"))
        }
      }
    }
  }
}