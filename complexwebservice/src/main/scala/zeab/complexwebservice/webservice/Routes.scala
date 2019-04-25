package zeab.complexwebservice.webservice

//Imports
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.{MediaTypeNegotiator, UnsupportedRequestContentTypeRejection}
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
  def allRoutes(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route =
    actorAsSourceForHttpStreamRoute ~ webSocket ~ returnBasedOnHeader

  //Routes dealing with basic ingress checks
  def actorAsSourceForHttpStreamRoute(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("actorassourceforhttpstream") {
      get {
        val sourceFeeder: ActorRef = actorSystem.actorOf(Props(classOf[ExampleDataPacketFeederActor]))
        val sourceGraph: ExampleDataPacketGraph = new ExampleDataPacketGraph(sourceFeeder)
        val source: Source[ByteString, NotUsed] = Source.fromGraph(sourceGraph)
          .map(_.asJson.noSpaces)
          .map(s => ByteString(s))
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, source))
      }
    }
  }

  def webSocket(implicit actorSystem: ActorSystem): Route = {
    class NewMsg{def msg = UUID.randomUUID().toString}
    pathPrefix("websocket") {
      get {
        val incomingMessages: Sink[Message, Future[Done]] =
          Sink
            .foreach { message => println(s"$message") }
        val outgoingMessages: Source[Message, NotUsed] =
          Source
            .repeat(new NewMsg)
            .map(msg => TextMessage(msg.msg))
            .throttle(1, 1.second)
        handleWebSocketMessages(Flow.fromSinkAndSource(incomingMessages, outgoingMessages))
      }
    }
  }

  //need to accept json or xml and decode correctly
//  def handleReq(json: String) = {
//    (get & extract(_.request.acceptedMediaRanges)) {
//      r =>
//        val encoding: MediaRange =
//          r.intersect(myEncodings).headOption
//            .getOrElse(MediaTypes.`application/json`)
//        complete {
//          // check conditions here
//          HttpResponse(entity = HttpEntity(encoding.specimen, json)) //
//        }
//    }
//  }
  val myEncodings = Seq(MediaRange(MediaTypes.`application/xml`),MediaRange(MediaTypes.`application/json`))

  //need to return the response based on the header
  def returnBasedOnHeader(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("returntype") {
      get {

        //if the header is xml send back xml...
        //if the header is json send back json...

        val rt : ToResponseMarshallable = Boop("llama", 10).toString

        val ww = <moose>asd</moose>
        val kk = HttpResponse(entity = HttpEntity(ContentType.WithCharset(MediaTypes.`application/xml`, HttpCharsets.`UTF-8`), ww.toString))
        complete(kk)
      }
    }
  }


//  def handleReq(json: String) = {
//    (get & extract(_.request.acceptedMediaRanges)) {
//      r =>
//        val encoding: MediaRange =
//          r.intersect(myEncodings).headOption
//            .getOrElse(MediaTypes.`application/json`)
//        complete {
//          // check conditions here
//          // HttpResponse(entity = HttpEntity(encoding.specimen, json)) //
//        }
//    }
//  }

}
