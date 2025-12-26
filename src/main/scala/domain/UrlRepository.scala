package domain

import domain.models.{MappedUrl, OriginalUrl, ShortCode}

trait UrlRepository {
  def save(mappedUrl: MappedUrl): Unit
  def findByShortCode(code: ShortCode): Option[MappedUrl]
  def findByOriginalUrl(url: OriginalUrl): Option[MappedUrl]
}
