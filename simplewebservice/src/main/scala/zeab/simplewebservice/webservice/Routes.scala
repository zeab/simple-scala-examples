package zeab.simplewebservice.webservice

//Imports
import zeab.akkatools.webservice.K8Routes._
//Java
import java.util.UUID
//Akka
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.util.ByteString
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Routes {

  //Collection of all the routes together in 1 big route
  def allRoutes(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route =
    ingressCheckRoute ~ examplesRoute ~ streamRoute ~ webUiRoute ~ readinessCheck() ~ livenessCheck()

  //Routes for serving files though a route
  def webUiRoute(implicit actorSystem: ActorSystem): Route = {
    //Serve up the web page with a get or a redirect if it cannot be found
    get {
      (pathEndOrSingleSlash & redirectToTrailingSlashIfMissing(StatusCodes.TemporaryRedirect)) {
        getFromFile("./webUi/index.html")
      } ~ {
        getFromDirectory("./webUi")
      }
    }
  }

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

  def examplesRoute(implicit system: ActorSystem): Route =
    pathPrefix("examples") {
      path(Segment) { id =>
        get {
          complete(s"get $id")
        } ~
          post {
            decodeRequest {
              entity(as[String]) { request =>
                complete(s"post $id")
              }
            }
          } ~
          put {
            decodeRequest {
              entity(as[String]) { request =>
                complete(s"put $id")
              }
            }
          } ~
          delete {
            decodeRequest {
              entity(as[String]) { request =>
                complete(s"delete $id")
              }
            }
          }
      } ~
        get {
          complete("get")
        } ~
        post {
          decodeRequest {
            entity(as[String]) { request =>
              complete("post")
            }
          }
        } ~
        put {
          decodeRequest {
            entity(as[String]) { request =>
              complete("put")
            }
          }
        } ~
        delete {
          decodeRequest {
            entity(as[String]) { request =>
              complete("delete")
            }
          }
        }
    }

  def streamRoute(implicit actorSystem: ActorSystem, mat: ActorMaterializer, executionContext: ExecutionContext): Route = {
    pathPrefix("stream") {
      get {
        val source = Source.repeat(Msg())
        val byteStringSource = source
          .throttle(elements = 6, per = 250.millisecond, maximumBurst = 10, mode = ThrottleMode.Shaping)
          .map(_.newMsg)
          .map(s => ByteString(s + "\n"))
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, byteStringSource))
      }
    }
  }

  case class Msg() {
    def newMsg: String = s"Ahoy ${UUID.randomUUID}"
  }

}
