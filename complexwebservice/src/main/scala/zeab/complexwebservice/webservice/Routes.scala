package zeab.complexwebservice.webservice

//Imports
import zeab.complexwebservice.webservice.actorassourceforhttpstream.{ExampleDataPacketFeederActor, ExampleDataPacketGraph}
//Akka
import akka.{Done, NotUsed}
import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
//Java
import java.util.UUID
//Scala
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
//Circe and Akka-Http plugin
import io.circe.generic.auto._
import io.circe.syntax._

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route = actorAsSourceForHttpStreamRoute ~ webSocket

  //Routes dealing with basic ingress checks
  def actorAsSourceForHttpStreamRoute(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("actorassourceforhttpstream") {
      get {
        val sourceFeeder: ActorRef = actorSystem.actorOf(Props(classOf[ExampleDataPacketFeederActor]))
        val sourceGraph: ExampleDataPacketGraph = new ExampleDataPacketGraph(sourceFeeder)
        val source: Source[ByteString, NotUsed] = Source.fromGraph(sourceGraph)
          .map(_.asJson.noSpaces)
          .map(s => ByteString(s + "\n"))
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, source))
      }
    }
  }

  def webSocket(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("websocket") {
      get {
        val incomingMessages: Sink[Message, Future[Done]] =
          Sink
            .foreach { message => println(s"$message") }
        //so the source... custom flow again ... right...
        //But then again... do i really need that... i really dont know...
        val outgoingMessages: Source[Message, NotUsed] =
          Source
            .repeat(TextMessage(s"${UUID.randomUUID}"))
            .throttle(1, 1.second)
        handleWebSocketMessages(Flow.fromSinkAndSource(incomingMessages, outgoingMessages))
      }
    }
  }

}
