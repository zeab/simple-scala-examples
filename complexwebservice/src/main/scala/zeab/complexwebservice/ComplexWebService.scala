package zeab.complexwebservice

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.webservice.WebServiceMessages.StartService
import zeab.akkatools.webservice.{WebServiceActor, WebServiceEnvGrok}
import zeab.complexwebservice.webservice.Routes
import zeab.logging.Logging
//Akka
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ComplexWebService extends Logging with WebServiceEnvGrok {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system: ActorSystem = ActorSystem("ComplexWebService", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Web Service
    system.actorOf(Props(classOf[WebServiceActor], mat), "ComplexWebService") ! StartService(Routes.allRoutes)

    //Start a scheduler that posts messages to the event bus so that the stream's can listen in
    system.scheduler.schedule(0.second, 1.second) {
      system.eventStream.publish(ExampleDataPacket("ahoy!"))
    }

  }

}
