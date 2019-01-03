package zeab.actorassourceforroute

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.duration._

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem): Route = streamRoute

  //Routes dealing with basic ingress checks
  def streamRoute(implicit actorSystem: ActorSystem): Route = {
    pathPrefix("stream") {
      get {

        val sourceOfNumbers = Source(1 to 1000000)
        val sourceOfDetailedMessages =
          sourceOfNumbers.map(num => ByteString(UUID.randomUUID() + s"Hello $num"))
            .throttle(elements = 1000, per = 1.second, maximumBurst = 1, mode = ThrottleMode.Shaping)

        complete(StatusCodes.OK)

      }
    }
  }

}