import sbt._
import Keys._

object EchoServerBuild extends Build {
  val appVersion = "1.0"

  val dependencies = Seq(
    "com.typesafe.akka" %% "akka-stream-experimental" % "2.0-M2",
    "com.typesafe.akka" %% "akka-http-core-experimental" % "2.0-M2",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.0-M2",
    "com.typesafe" % "config" % "1.2.1"
  )

  val echoServer: Project = Project(
    "echo-server",
    file("."),
    settings = Seq(
      organization := "com.danieltrinh",
      name := "echo-server",
      version in ThisBuild := "0.1.0",
      scalaVersion := "2.11.7",
      libraryDependencies ++= dependencies,
      scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-Xlint",
        "-language:_",
        "-target:jvm-1.6",
        "-encoding", "UTF-8"
      )
    )
  )

  val httpRequester: Project = Project(
    "http_requester",
    file("http_requester"),
    settings = Seq(
      organization := "com.danieltrinh",
      name := "http-requester",
      scalaVersion := "2.11.7",
      version in ThisBuild := "0.1.0",
      libraryDependencies ++= dependencies
    )
  )
}