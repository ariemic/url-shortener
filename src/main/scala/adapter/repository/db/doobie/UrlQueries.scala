package adapter.repository.db.doobie

import domain.models.{MappedUrl, OriginalUrl, ShortCode}
import doobie.implicits.*
import doobie.{Query0, Update0}


object UrlQueries {
  
  def insert(mappedUrl: MappedUrl): Update0 =
    sql"""
      INSERT INTO url_mappings (short_code, original_url)
      VALUES (${mappedUrl.shortCode.value}, ${mappedUrl.originalUrl.value})
    """.update
  
  def selectByShortCode(code: ShortCode): Query0[MappedUrl] =
    sql"""
      SELECT short_code, original_url
      FROM url_mappings
      WHERE short_code = ${code.value}
    """.query[(String, String)]
      .map { case (sc, url) => MappedUrl(ShortCode(sc), OriginalUrl(url)) }
  
  def selectByOriginalUrl(url: OriginalUrl): Query0[MappedUrl] =
    sql"""
      SELECT short_code, original_url
      FROM url_mappings
      WHERE original_url = ${url.value}
    """.query[(String, String)]
      .map { case (sc, originalUrl) => MappedUrl(ShortCode(sc), OriginalUrl(originalUrl)) }
}
