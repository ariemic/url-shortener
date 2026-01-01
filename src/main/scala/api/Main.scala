package api

import adapter.generator.Base62ShortCodeGenerator
import adapter.repository.h2.{DatabaseH2Config, DbH2UrlRepository}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import domain.{ShortCodeGenerator, UrlFacade, UrlMapper, UrlRepository}
import doobie.h2.H2Transactor
import doobie.util.transactor.Transactor

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Main extends App {

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "url-shortener-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  // Initialize database
  println("🗄️  Initializing database...")
  val config = DatabaseH2Config.load
  
  val xa: Transactor[IO] = H2Transactor.newH2Transactor[IO](
    url = config.url,
    user = config.user,
    pass = config.password,
    connectEC = ExecutionContext.global
  ).allocated.unsafeRunSync()._1

  DatabaseH2Config.initialize(xa, config.schema).unsafeRunSync()
  
  println("✅ Database initialized!")

  // Wire up your dependencies (Dependency Injection)
  val generator: ShortCodeGenerator = new Base62ShortCodeGenerator()

  // Phase 3: Use database repository instead of in-memory
  val repository: UrlRepository = new DbH2UrlRepository(xa)
  // val repository: UrlRepository = new InMemoryUrlRepository()  // Phase 1/2

  val urlMapper: UrlMapper = new UrlMapper(generator, repository)
  val urlFacade: UrlFacade = new UrlFacade(urlMapper, repository)
  val routes: Route = new Routes(urlFacade).routes

  // Start the HTTP server
  val host = "localhost"
  val port = 8080

  val bindingFuture: Future[Http.ServerBinding] = Http()
    .newServerAt(host, port)
    .bind(routes)

  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(s"✅ Server online at http://${address.getHostString}:${address.getPort}/")
      println(s"📝 Try: POST http://localhost:8080/api/shorten")
      println(s"   Body: {\"url\": \"https://google.com\"}")
    case Failure(exception) =>
      println(s"❌ Failed to bind HTTP server: ${exception.getMessage}")
      system.terminate()
  }

  // Shutdown hook
  scala.sys.addShutdownHook {
    println("\n🛑 Shutting down server...")
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
