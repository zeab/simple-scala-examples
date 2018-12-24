package zeab.simpleudpservice

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.udp.service.UdpServiceActor
import zeab.akkatools.udp.service.UdpServiceMessages.StartUdpServer
import zeab.logging.Logging
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.ExecutionContext

object SimpleUdpService extends Logging{

  def main(args: Array[String]): Unit = {

    log.info("Starting Simple Udp Service")

    //Actor System
    implicit val actorSystem:ActorSystem = ActorSystem("SimpleUdpService", AkkaConfigBuilder.buildConfig())
    implicit val executionContext:ExecutionContext = actorSystem.dispatcher
    implicit val actorMaterilizer:ActorMaterializer = ActorMaterializer()

    //Udp Service
    val udpService: ActorRef = actorSystem.actorOf(Props(classOf[UdpServiceActor]))

    //Tell the service to start
    udpService ! StartUdpServer()

  }

}
