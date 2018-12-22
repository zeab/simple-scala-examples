package zeab.simpleudpclient

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.udp.client.UdpClientMessages.{SendUdpDatagram, SendUdpDatagramToHost}
import zeab.logging.Logging
//Java
import java.util.concurrent.atomic.AtomicInteger
//Akka
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object SimpleUdpClient extends Logging with UdpClientEnvGrok {

  def main(args: Array[String]): Unit = {

    //Keep a count if we need it
    val count: AtomicInteger = new AtomicInteger()

    implicit val actorSystem: ActorSystem = ActorSystem("SimpleUdpClient", AkkaConfigBuilder.buildConfig())
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher
    implicit val actorMaterilizer: ActorMaterializer = ActorMaterializer()

    //Udp Client
    val udpClient: ActorRef = createUdpClient()

    //Start up a scheduler to send the udp datagram on a timer
    actorSystem.scheduler.schedule(1.second, udpClientEmitDelayInMs.milli) {
      for (_ <- 1 to udpClientDatagramConcurrentCount) {
        Future {
          //Checks if the datagram has the special regex count value to be replaced with an actual count
          val countPattern: String = "<count>"
          countPattern.r.findFirstMatchIn(udpClientDataGram) match {
            case Some(_) =>
              val countReplacementDatagram: String = udpClientDataGram.replaceAll(countPattern, count.incrementAndGet().toString)
              log.info(s"Sending $countReplacementDatagram to $udpClientHost:$udpClientPort")
              if (isUdpClientConnected) udpClient ! SendUdpDatagram(countReplacementDatagram)
              else udpClient ! SendUdpDatagramToHost(countReplacementDatagram, udpClientHost, udpClientPort)
            case None =>
              log.info(s"Sending $udpClientDataGram to $udpClientHost:$udpClientPort")
              if (isUdpClientConnected) udpClient ! SendUdpDatagram(udpClientDataGram)
              else udpClient ! SendUdpDatagramToHost(udpClientDataGram, udpClientHost, udpClientPort)
          }
        }
      }
    }

  }

}
