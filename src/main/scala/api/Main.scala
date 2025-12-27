package api

import adapter.generator.HashBasedShortCodeGenerator
import adapter.repository.inmemory.InMemoryUrlRepository
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import domain.{ShortCodeGenerator, UrlFacade, UrlMapper, UrlRepository}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Main extends App {

  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "url-shortener-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  // Wire up your dependencies (Dependency Injection)
  val generator: ShortCodeGenerator = new HashBasedShortCodeGenerator()
  val repository: UrlRepository = new InMemoryUrlRepository()
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
