name := """Rest_Play_Mongo"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

assemblyJarName in assembly := "scalaPlay.jar"

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.bintrayRepo("scalaz", "releases")
)

libraryDependencies ++= Seq(
  ws,
  specs2 % Test,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test,
  "org.reactivemongo" %% "reactivemongo" % "0.12.5",
  "io.swagger" % "swagger-play2_2.11" % "1.6.0-SNAPSHOT"
  )

PlayKeys.devSettings := Seq("play.server.http.port" -> "8080")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*)                 => MergeStrategy.discard
  case PathList("reference.conf")                    => MergeStrategy.concat
  case "application.conf"                            => MergeStrategy.concat
  case x => MergeStrategy.first
}

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
