package adapter.repository.db.doobie

import adapter.repository.db.doobie.UrlQueries.*
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import domain.UrlRepository
import domain.models.{MappedUrl, OriginalUrl, ShortCode}
import doobie.implicits.*
import doobie.util.transactor.Transactor


class DoobieUrlRepository(xa: Transactor[IO]) extends UrlRepository {

  override def save(mappedUrl: MappedUrl): Unit =
    insert(mappedUrl)
      .run
      .transact(xa)
      .unsafeRunSync()

  override def findByShortCode(code: ShortCode): Option[MappedUrl] =
    selectByShortCode(code)
      .option
      .transact(xa)
      .unsafeRunSync()

  override def findByOriginalUrl(url: OriginalUrl): Option[MappedUrl] =
    selectByOriginalUrl(url)
      .option
      .transact(xa)
      .unsafeRunSync()
}
