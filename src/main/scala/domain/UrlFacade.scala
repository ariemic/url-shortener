package domain

import domain.models.{MappedUrl, OriginalUrl, ShortCode}

class UrlFacade(
                 urlMapper: UrlMapper,
                 repository: UrlRepository
               ) {
  def shorten(url: OriginalUrl): Either[UrlShortenerError, MappedUrl] = {
    urlMapper.shorten(url)
  }

  def redirect(code: ShortCode): Option[OriginalUrl] =
    repository.findByShortCode(code).map(_.originalUrl)

}
