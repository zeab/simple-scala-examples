package zeab.simplekafkaenricher

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.{BasicLogMessage, Logging}
//Java
import java.util.concurrent.Executors
//Akka
import akka.actor.ActorSystem
import akka.kafka.ConsumerMessage.CommittableOffsetBatch
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka._
import akka.kafka.scaladsl.Consumer.DrainingControl
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink}
import com.typesafe.config.Config
//Kafka
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer, StringSerializer}
//Scala
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
//Circe
import io.circe.generic.auto._
import io.circe.syntax._

object SimpleKafkaEnricher extends Logging {

  def main(args: Array[String]): Unit = {

    //Settings
    val kafkaAddress: String = ""
    val kafkaConcurrentCount: Int = 750
    val kafkaConsumerDelayInMs: Int = 1000
    val kafkaConsumeTopic: String = "mytopic1"
    val kafkaProduceTopic: String = "mytopic2"
    val kafkaConsumerGroup: String = "enricher"
    val kafkaSteamCount: Int = 3

    //Akka
    implicit val actorSystem: ActorSystem = ActorSystem("SimpleKafkaEnricher", AkkaConfigBuilder.buildConfig())
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1024))

    //Kafka Producer Settings
    val config1: Config = actorSystem.settings.config.getConfig("akka.kafka.producer")
    val producerSettings: ProducerSettings[String, String] =
      ProducerSettings(config1, new StringSerializer, new StringSerializer)
        .withBootstrapServers(kafkaAddress)
    val kafkaProducer: KafkaProducer[String, String] = producerSettings.createKafkaProducer()

    //Kafka Consumer Settings
    val kafkaConsumerConfig: Config = actorSystem.settings.config.getConfig("akka.kafka.consumer")
    val consumerSettings: ConsumerSettings[String, Array[Byte]] =
      ConsumerSettings(kafkaConsumerConfig, new StringDeserializer, new ByteArrayDeserializer)
        .withBootstrapServers(kafkaAddress)
        .withGroupId(kafkaConsumerGroup)

    for (_ <- 1 to kafkaSteamCount) {
      //Stream
      Consumer
        .committableSource(consumerSettings, Subscriptions.topics(kafkaConsumeTopic))
        .throttle(kafkaConcurrentCount, kafkaConsumerDelayInMs.millisecond)
        .mapAsync(100) { msg =>
          Future {
            log.info(BasicLogMessage(s"${msg.committableOffset.partitionOffset.offset} enriching").asJson.noSpaces)
            ProducerMessage.Message[String, String, ConsumerMessage.CommittableOffset](
              new ProducerRecord(kafkaProduceTopic, msg.record.value.map(_.toChar).mkString + " llama!!! " + msg.committableOffset.partitionOffset.offset),
              msg.committableOffset
            )
          }
        }.async
        .via {
          Producer.flexiFlow(producerSettings)
        }
        .map(_.passThrough)
        .batch(max = kafkaConcurrentCount / 4, CommittableOffsetBatch.apply)(_.updated(_))
        .mapAsync(8)(_.commitScaladsl())
        .toMat(Sink.ignore)(Keep.both)
        .mapMaterializedValue(DrainingControl.apply)
        .run()

    }

  }

}