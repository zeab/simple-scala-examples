package zeab.complexwebservice.webservice

//Imports
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source
import akka.util.ByteString
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route = ingressCheckRoute ~ streamRoute

  //Routes dealing with basic ingress checks
  def ingressCheckRoute(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("ingressCheck") {
      get {
        complete(StatusCodes.OK, "Get IngressCheck")
      } ~
        post {
          complete(StatusCodes.Created, "Post IngressCheck")
        } ~
        put {
          complete(StatusCodes.Accepted, "Put IngressCheck")
        } ~
        delete {
          complete(StatusCodes.OK, "Delete IngressCheck")
        }
    }
  }

  def streamRoute(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route = {
    pathPrefix("stream") {
      get {
          val sourceOfInformation = Source("Prepare to scroll!")
          val sourceOfNumbers = Source(1 to 1000000)
          val byteStringSource = sourceOfInformation.concat(sourceOfNumbers) // merge the two sources
            .throttle(elements = 1000, per = 1.second, maximumBurst = 1, mode = ThrottleMode.Shaping)
            .map(_.toString)
            .map(s => ByteString(s + "\n"))
          complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, byteStringSource))
      }
    }
  }

}
