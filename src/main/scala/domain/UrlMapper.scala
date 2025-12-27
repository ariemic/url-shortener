package domain

import domain.models.{MappedUrl, OriginalUrl}

class UrlMapper(generator: ShortCodeGenerator, repository: UrlRepository) {

  def shorten(originalUrl: OriginalUrl): Either[UrlShortenerError, MappedUrl] = {

    //todo handle collision of generated code
    repository.findByOriginalUrl(originalUrl) match {
      case Some(existing) => Right(existing) //validation: code for url already exists
      case None =>
        val code = generator.generate(Some(originalUrl))
        val mappedUrl = MappedUrl(code, originalUrl)
        repository.save(mappedUrl)
        Right(mappedUrl)
    }
  }


}








