package zeab.SimpleKafkaProducer

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.udp.client.UdpClientMessages.SendUdpDatagram
import zeab.akkatools.udp.client.UdpConnectedClientActor
import zeab.logging.Logging
//Akka
import akka.actor.{ActorSystem, ActorRef, Props}
import akka.routing.RoundRobinPool
import akka.{NotUsed, Done}
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{Source, Keep, Sink}
import akka.kafka.ProducerSettings
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
//Kafka
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
//Scala
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object SimpleKafkaProducer extends Logging {

  def main(args: Array[String]): Unit = {

    //Settings
    val kafkaAddress: String = getEnvVar[String]("KAFKA_ADDRESS", "localhost:9092")
    val kafkaProducerTopic: String = getEnvVar[String]("KAFKA_PRODUCER_TOPIC", "my-topic")
    val kafkaMessagesPerSecond: Int = getEnvVar[Int]("KAFKA_MESSAGES_PER_SECOND", 1)
    val kafkaMetricProducerLabel: String = getEnvVar[String]("KAFKA_METRIC_PRODUCER_LABEL", "kafka.produce")
    val kafkaMetricTags: String = getEnvVar[String]("KAFKA_METRIC_TAGS", "")
    val isUdpClientEmit: Boolean = getEnvVar[Boolean]("IS_UDP_CLIENT_EMIT", false)
    val udpClientHost: String = getEnvVar[String]("UDP_CLIENT_HOST", "localhost")
    val udpClientPort: String = getEnvVar[String]("UDP_CLIENT_PORT", "8125")

    //Akka
    implicit val system: ActorSystem = ActorSystem("SimpleKafkaProducer", AkkaConfigBuilder.buildConfig())
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher

    //Udp Client
    val udpClient: ActorRef =
      system.actorOf(
        RoundRobinPool(5).props(Props(
            classOf[UdpConnectedClientActor],
            udpClientHost,
            udpClientPort)
          ),
        "UdpClient"
      )

    //Kafka Producer Settings
    val producerConfig: Config = system.settings.config.getConfig("akka.kafka.producer")
    val producerSettings: ProducerSettings[String, String] =
      ProducerSettings(producerConfig, new StringSerializer, new StringSerializer)
        .withBootstrapServers(kafkaAddress)

    //Source
    val kafkaSource: Source[Msg, NotUsed] = Source.repeat(new Msg)

    //Sink
    val kafkaSink: Sink[ProducerRecord[String, String], Future[Done]] = Producer.plainSink(producerSettings)

    //Stream
    kafkaSource
      .throttle(kafkaMessagesPerSecond, 1.second)
      .mapAsync(100){newMsg =>
        if (isUdpClientEmit) udpClient ! SendUdpDatagram(s"$kafkaMetricProducerLabel|1|$kafkaMetricTags")
        val msg: String = newMsg.msg
        log.info(msg)
        Future{new ProducerRecord[String, String](kafkaProducerTopic, msg)}
      }.async
      .toMat(kafkaSink)(Keep.both)
      .run()

  }

}
