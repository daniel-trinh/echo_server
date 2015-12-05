package com.danieltrinh.echo_server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

object HelloWorld extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val serverSource = Http().bind(interface = "127.0.0.1", port = 7059)
  final val response = HttpResponse(204)
  final val requestHandler: HttpRequest => HttpResponse = {
    case _: HttpRequest =>
      response
  }

  val bindingFuture: Future[Http.ServerBinding] =
    serverSource.log(".").to(Sink.foreach { connection =>
      println(".")
      connection handleWithSyncHandler requestHandler
    }).run()
}