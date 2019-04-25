package zeab.simplekafkaenricher

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.udp.client.UdpClientMessages.SendUdpDatagram
import zeab.akkatools.udp.client.UdpConnectedClientActor
import zeab.logging.Logging
//Java
import java.util.concurrent.Executors
//Akka
import akka.actor.{ActorSystem, ActorRef, Props}
import akka.routing.RoundRobinPool
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka._
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}
import com.typesafe.config.Config
//Kafka
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer, StringSerializer}
//Scala
import scala.concurrent.{ExecutionContext, Future}

object SimpleKafkaEnricher extends Logging {

  def main(args: Array[String]): Unit = {

    //Settings
    val kafkaAddress: String = getEnvVar[String]("KAFKA_ADDRESS", "localhost:9092")
    val kafkaConsumerGroup: String = getEnvVar[String]("KAFKA_CONSUMER_GROUP", "my-enrich-group")
    val kafkaConsumerTopic: String = getEnvVar[String]("KAFKA_CONSUMER_TOPIC", "my-topic")
    val kafkaProducerTopic: String = getEnvVar[String]("KAFKA_PRODUCER_TOPIC", "my-enriched-topic")
    val kafkaMetricConsumerLabel: String = getEnvVar[String]("KAFKA_METRIC_CONSUMER_LABEL", "kafka.consume")
    val kafkaMetricProducerLabel: String = getEnvVar[String]("KAFKA_METRIC_PRODUCER_LABEL", "kafka.produce")
    val kafkaMetricTags: String = getEnvVar[String]("KAFKA_METRIC_TAGS", "")
    val isKafkaEarliest: Boolean = getEnvVar[Boolean]("IS_KAFKA_EARLIEST", false)
    val isUdpClientEmit: Boolean = getEnvVar[Boolean]("IS_UDP_CLIENT_EMIT", false)
    val udpClientHost: String = getEnvVar[String]("UDP_CLIENT_HOST", "localhost")
    val udpClientPort: String = getEnvVar[String]("UDP_CLIENT_PORT", "8125")

    //Akka
    implicit val system: ActorSystem = ActorSystem("SimpleKafkaEnricher", AkkaConfigBuilder.buildConfig())
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1024))

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

    //Stream
    Consumer
      .committableSource(consumerSettings, Subscriptions.topics(kafkaConsumerTopic))
      .mapAsync(100) { incomingMsg =>
        Future {
          val outgoingMsg: String = incomingMsg.record.value.map(_.toChar).mkString + " enriched!"
          if (isUdpClientEmit) {
            udpClient ! SendUdpDatagram(s"$kafkaMetricProducerLabel|1|$kafkaMetricTags")
            udpClient ! SendUdpDatagram(s"$kafkaMetricConsumerLabel|1|$kafkaMetricTags")
          }
          log.info("CONSUME-" + incomingMsg.record.value.map(_.toChar).mkString)
          log.info("PRODUCE-" + outgoingMsg)
          ProducerMessage.Message[String, String, ConsumerMessage.CommittableOffset](
            new ProducerRecord(kafkaProducerTopic, outgoingMsg),
            incomingMsg.committableOffset
          )
        }
      }.async
      .via ( Producer.flexiFlow(producerSettings) )
      .map(_.passThrough)
      .batch(max = 5, CommittableOffsetBatch.apply)(_.updated(_))
      .mapAsync(100){_.commitScaladsl()}
      .toMat(Sink.ignore)(Keep.both)
      .mapMaterializedValue(DrainingControl.apply)
      .run()

  }

}