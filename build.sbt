ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "url-shortener",
    libraryDependencies ++= Seq(
      // Akka HTTP for REST API
      "com.typesafe.akka" %% "akka-http" % "10.5.3",
      "com.typesafe.akka" %% "akka-stream" % "2.8.5",
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",

      // JSON support with spray-json
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3",

      // Testing
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % "10.5.3" % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.5" % Test,

      // Database - Doobie (Pure FP SQL library)
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC5",
      "org.tpolecat" %% "doobie-h2" % "1.0.0-RC5",
      "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC5",  // Connection pooling

      // Cats Effect (required by Doobie)
      "org.typelevel" %% "cats-effect" % "3.5.4",

      // H2 Database
      "com.h2database" % "h2" % "2.2.224",


      // library for loading config files
      "com.github.pureconfig" %% "pureconfig-core" % "0.17.9"

    )
  )
