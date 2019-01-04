package zeab.actorassourceforroute

//Imports
import java.util.UUID

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.webservice.WebServiceActor
import zeab.akkatools.webservice.WebServiceMessages.StartService
import zeab.logging.Logging

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object ActorAsSourceForRoute extends Logging{

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("ActorAsSourceForRoute", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Web Service
    system.actorOf(Props(classOf[WebServiceActor], mat), "ActorAsSourceForRouteWebService") ! StartService(Routes.allRoutes)

    system.scheduler.schedule(1.second, 1.second){
//      system.eventStream.publish(s"${UUID.randomUUID}")
      system.eventStream.publish(StreamDataPacket(UUID.randomUUID.toString, "llama"))
    }

  }

}
