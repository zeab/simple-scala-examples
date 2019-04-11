package zeab.simplewebservice

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.webservice.{WebServiceActor, WebServiceEnvGrok}
import zeab.akkatools.webservice.WebServiceMessages.StartService
import zeab.heapmonitor.HeapMonitorActor
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
    implicit val system:ActorSystem = ActorSystem("SimpleWebService", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Web Service
    system.actorOf(Props(classOf[WebServiceActor], mat), "SimpleWebService") ! StartService(Routes.allRoutes, "8082")

    //Start a heap monitor just for fun
    system.actorOf(Props(classOf[HeapMonitorActor], 5, getEnvVar("IS_HEAP_LOGGED", "true").toBoolean))

  }

}
