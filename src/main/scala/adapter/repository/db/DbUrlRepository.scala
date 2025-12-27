package adapter.repository.db

import domain.UrlRepository
import domain.models.{MappedUrl, OriginalUrl, ShortCode}

class DbUrlRepository extends UrlRepository {
  override def save(mappedUrl: MappedUrl): Unit = ???

  override def findByShortCode(code: ShortCode): Option[MappedUrl] = ???

  override def findByOriginalUrl(url: OriginalUrl): Option[MappedUrl] = ???
}
