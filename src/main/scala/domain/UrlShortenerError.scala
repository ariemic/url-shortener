package domain

sealed trait UrlShortenerError

object UrlShortenerError {
  //is this about original or mapped?
  case class InvalidUrl(message: String) extends UrlShortenerError
  //do I need this, I can just return url found in db
  case class UrlAlreadyExists(message: String) extends UrlShortenerError
}
