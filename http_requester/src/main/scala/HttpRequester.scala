package com.brightroll.vulcan_loader
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.{OverflowStrategy, FlowShape, ActorMaterializer}
import akka.stream.scaladsl.{Tcp, Sink, Source}
import scala.concurrent.Future

object HttpRequester extends App {
  implicit val system = ActorSystem("vulcan-loader")
  implicit val ctx = system.dispatcher
  implicit val materializer = ActorMaterializer()


  def run: Future[Long] = {
    println("Starting loading process..")
    val startTime = System.currentTimeMillis()

    Source.repeat("http://localhost:7059/1/12345?a=1&b=2")
      .mapAsync(64) { uri =>
        print(".")
        val request = HttpRequest(HttpMethods.POST, uri)
        Http().singleRequest(HttpRequest(HttpMethods.POST, uri)).map { (request, _) }.flatMap {
          case (request, response @ HttpResponse(status, _, _, _)) if status.intValue() != 204 && status.intValue() != 201 =>
            println(s"failed request: $response")
            Http().singleRequest(request)
          case _ =>
            Future.successful()
        }
      }.grouped(10000)
      .runWith(Sink.fold(0L) { case (accu, _) =>
        val timeElapsed = System.currentTimeMillis() - startTime
        val res = accu + 10000L
        println(s"$res requests sent. Time elapsed: $timeElapsed ms.")
        res
      }).map { x =>
      println("Processing finished, exiting...")
      system.shutdown()
      materializer.shutdown()
      sys.exit(0)
    }
  }
  run
}