package zeab.simplewebservice

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.webservice.{WebServiceActor, WebServiceEnvGrok}
import zeab.akkatools.webservice.WebServiceMessages.StartService
import zeab.logging.Logging
import zeab.simplewebservice.webservice.Routes
//Akka
import akka.actor.Props
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.ExecutionContext

object SimpleWebService extends Logging with WebServiceEnvGrok{

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val actorSystem:ActorSystem = ActorSystem("SimpleWebService", AkkaConfigBuilder.buildConfig())
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher

    //Web Service
    actorSystem.actorOf(Props(classOf[WebServiceActor], actorMaterializer), "SimpleWebService") ! StartService(Routes.allRoutes, "8082")

  }

}
