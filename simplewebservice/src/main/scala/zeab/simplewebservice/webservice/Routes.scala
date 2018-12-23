package zeab.simplewebservice.webservice

//Imports
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem): Route = ingressCheckRoute

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

}
