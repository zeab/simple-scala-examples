package zeab.complexwebservice.webservice

//Imports
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.util.ByteString
import zeab.complexwebservice.webservice.actorassourceforhttpstream.{ExampleDataPacketFeederActor, ExampleDataPacketGraph}
//Scala
import scala.concurrent.ExecutionContext
//Circe and Akka-Http plugin
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.circe.syntax._

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route = actorAsSourceForHttpStreamRoute

  //Routes dealing with basic ingress checks
  def actorAsSourceForHttpStreamRoute(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("actorassourceforhttpstream") {
      get {
        val sourceFeeder: ActorRef = actorSystem.actorOf(Props(classOf[ExampleDataPacketFeederActor]))
        val sourceGraph: ExampleDataPacketGraph = new ExampleDataPacketGraph(sourceFeeder)
        val source = Source.fromGraph(sourceGraph)
          .map(_.asJson.noSpaces)
          .map(s => ByteString(s + "\n"))
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, source))
      }
    }
  }

}
