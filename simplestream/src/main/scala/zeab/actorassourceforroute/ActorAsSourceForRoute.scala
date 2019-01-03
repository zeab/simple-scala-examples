package zeab.actorassourceforroute

//Imports
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.webservice.WebServiceActor
import zeab.akkatools.webservice.WebServiceMessages.StartService
import zeab.logging.Logging

import scala.concurrent.ExecutionContext

object ActorAsSourceForRoute extends Logging{

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("ActorAsSourceForRoute", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Web Service
    system.actorOf(Props(classOf[WebServiceActor], mat), "ActorAsSourceForRouteWebService") ! StartService(Routes.allRoutes)

  }

}
