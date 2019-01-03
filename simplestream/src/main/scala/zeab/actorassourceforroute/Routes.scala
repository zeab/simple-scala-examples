package zeab.actorassourceforroute

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.duration._

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem, mat: ActorMaterializer): Route = streamRoute

  //Routes dealing with basic ingress checks
  def streamRoute(implicit actorSystem: ActorSystem, mat: ActorMaterializer): Route = {
    pathPrefix("stream") {
      get {
        val sourceFeeder: ActorRef = actorSystem.actorOf(Props(classOf[SourceFeeder]))
        val sourceGraph: MessageSource = new MessageSource(sourceFeeder)
        val source = Source.fromGraph(sourceGraph)
          .map(_.toString)
          .map(s => ByteString(s + "\n"))
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, source))
      }
    }
  }

}