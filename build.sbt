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
      "org.scalameta" %% "munit" % "1.0.0" % Test
    )
  )
