package adapter.repository.db.doobie
import cats.effect.IO
import cats.implicits.*
import doobie.*
import doobie.implicits.*
import pureconfig.{ConfigReader, ConfigSource}

import scala.io.Source

case class DoobieConfig(
                           driver: String,
                           url: String,
                           user: String,
                           password: String,
                           poolSize: Int,
                           schema: String
                         ) derives ConfigReader


object DoobieConfig {

  def load: DoobieConfig =
    ConfigSource.default.at("database").loadOrThrow[DoobieConfig]

  def initialize(xa: Transactor[IO], schemaPath: String): IO[Unit] = {
    IO {
      val source = Source.fromFile(schemaPath)
      try source.mkString finally source.close()
    }.flatMap { schemaSql =>

      val statements = schemaSql.split(";").map(_.trim).filter(_.nonEmpty)

      val ios = statements.map { stmt =>
        Fragment.const(stmt).update.run.transact(xa)
      }

      // Combine all IOs and ignore results
      ios.toList.sequence.void
    }
  }
}
