package zeab.simpleudpclient

//Imports
import zeab.akkatools.udp.client.{UdpConnectedClientActor, UdpUnconnectedClientActor}
import zeab.sys.EnvironmentVariables
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.RoundRobinPool

trait UdpClientEnvGrok extends EnvironmentVariables {

  val udpClientDataGram: String = getEnvVar[String]("UDP_CLIENT_DATAGRAM", "Ahoy!")
  val udpClientEmitDelayInMs: Int = getEnvVar[String]("UDP_CLIENT_DATAGRAM_EMIT_DELAY_IN_MS", "1000").toInt
  val udpClientDatagramConcurrentCount: Int = getEnvVar[String]("UDP_CLIENT_DATAGRAM_CONCURRENT_COUNT", "1").toInt

  val udpClientHost: String = getEnvVar[String]("UDP_CLIENT_HOST", "localhost")
  val udpClientPort: String = getEnvVar[String]("UDP_CLIENT_PORT", "8125")
  val isUdpClientConnected: Boolean = getEnvVar[String]("IS_UDP_CLIENT_CONNECTED", "true").toBoolean

  //Udp Client
  def createUdpClient(poolSize: Int = 5, isConnected: Boolean = isUdpClientConnected)(implicit actorSystem: ActorSystem): ActorRef =
    if (isConnected) {
      actorSystem.actorOf(
        RoundRobinPool(poolSize).
          props(Props(
            classOf[UdpConnectedClientActor],
            udpClientHost,
            udpClientPort)
          ), "UdpClient"
      )
    }
    else {
      actorSystem.actorOf(
        RoundRobinPool(poolSize).
          props(
            Props(
              classOf[UdpUnconnectedClientActor]
            )
          ),
        "UdpClient"
      )
    }

}
