package zeab.simplekafkaconsumer

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.udp.client.UdpClientMessages.SendUdpDatagram
import zeab.akkatools.udp.client.UdpConnectedClientActor
import zeab.logging.Logging
//Akka
import akka.actor.{ActorSystem, ActorRef, Props}
import akka.routing.RoundRobinPool
import akka.kafka.scaladsl.Consumer.Control
import akka.stream.scaladsl.Source
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
//Kafka
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import org.apache.kafka.clients.consumer.ConsumerRecord
//Scala
import scala.concurrent.ExecutionContext

object SimpleKafkaConsumer extends Logging {

  def main(args: Array[String]): Unit = {

    //Kafka
    val kafkaAddress: String = getEnvVar[String]("KAFKA_ADDRESS", "localhost:9092")
    val kafkaConsumerTopic: String = getEnvVar[String]("KAFKA_CONSUMER_TOPIC", "my-topic")
    val kafkaConsumerGroup: String = getEnvVar[String]("KAFKA_CONSUMER_GROUP", "my-group")
    val kafkaMetricConsumerLabel: String = getEnvVar[String]("KAFKA_METRIC_CONSUMER_LABEL", "kafka.consume")
    val kafkaMetricTags: String = getEnvVar[String]("KAFKA_METRIC_TAGS", "")
    val isKafkaEarliest: Boolean = getEnvVar[Boolean]("IS_KAFKA_EARLIEST", false)
    val isUdpClientEmit: Boolean = getEnvVar[Boolean]("IS_UDP_CLIENT_EMIT", false)
    val udpClientHost: String = getEnvVar[String]("UDP_CLIENT_HOST", "localhost")
    val udpClientPort: String = getEnvVar[String]("UDP_CLIENT_PORT", "8125")

    //Akka
    implicit val system: ActorSystem = ActorSystem("SimpleKafkaConsumer", AkkaConfigBuilder.buildConfig())
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

    //Kafka Consumer Settings
    val consumerConfig: Config = system.settings.config.getConfig("akka.kafka.consumer")

    val consumerSettings: ConsumerSettings[String, Array[Byte]] =
      if (isKafkaEarliest)
        ConsumerSettings(consumerConfig, new StringDeserializer, new ByteArrayDeserializer)
          .withBootstrapServers(kafkaAddress)
          .withGroupId(kafkaConsumerGroup)
          .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      else
        ConsumerSettings(consumerConfig, new StringDeserializer, new ByteArrayDeserializer)
          .withBootstrapServers(kafkaAddress)
          .withGroupId(kafkaConsumerGroup)

    //Source
    val kafkaSource: Source[ConsumerRecord[String, Array[Byte]], Control] =
      Consumer.plainSource(consumerSettings, Subscriptions.topics(kafkaConsumerTopic))

    //Run the Stream
    kafkaSource
      .map { record =>
        if (isUdpClientEmit) udpClient ! SendUdpDatagram(s"$kafkaMetricConsumerLabel|1|$kafkaMetricTags")
        "Offset:" + record.offset() + " Value:" + record.value.map(_.toChar).mkString
      }
      .runForeach( log.info )

  }

}
