package adapter.repository.h2
import cats.effect.IO
import domain.UrlRepository
import domain.models.{MappedUrl, OriginalUrl, ShortCode}
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import doobie.util.transactor.Transactor
import cats.effect.unsafe.implicits.global


class DbH2UrlRepository(xa: Transactor[IO]) extends UrlRepository {
  override def save(mappedUrl: MappedUrl): Unit =
    val insertQuery =
      sql"""
      INSERT INTO url_mappings (short_code, original_url)
      VALUES (${mappedUrl.shortCode.value}, ${mappedUrl.originalUrl.value})

     """.update
    insertQuery
    .run
      .transact(xa)
      .unsafeRunSync() // run the IO effect synchronously

  override def findByShortCode(code: ShortCode): Option[MappedUrl] = {
    sql"""
        SELECT short_code, original_url
        FROM url_mappings
        WHERE short_code = ${code.value}
      """
      .query[(String, String)]
      .option
      .transact(xa)
      .unsafeRunSync()
      .map { case (shortCode, originalUrl) =>
        MappedUrl(ShortCode(shortCode), OriginalUrl(originalUrl))
      }
  }


  override def findByOriginalUrl(url: OriginalUrl): Option[MappedUrl] = {
    sql"""
                SELECT short_code, original_url
                FROM url_mappings
                WHERE original_url = ${url.value}
               """
      .query[(String, String)]
      .option
      .transact(xa)
      .unsafeRunSync()
      .map { case (shortCode, originalUrl) =>
        MappedUrl(ShortCode(shortCode), OriginalUrl(originalUrl))
      }
  }

}
