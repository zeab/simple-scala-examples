package zeab.simplekafkaconsumer

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.{BasicLogMessage, Logging}
//Akka
import akka.actor.ActorSystem
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.kafka.scaladsl.Consumer
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
//Kafka
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
//Scala
import scala.concurrent.ExecutionContext
//Circe
import io.circe.generic.auto._
import io.circe.syntax._

object SimpleKafkaConsumer extends Logging{

  def main(args: Array[String]): Unit = {

    //Kafka
    val kafkaAddress: String = ""
    val kafkaConsumerGroup: String = "example"
    val kafkaTopic: String = "my-topic"

    //Akka
    implicit val system: ActorSystem = ActorSystem("SimpleKafkaConsumer", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Kafka Consumer Settings
    val kafkaConsumerConfig: Config = system.settings.config.getConfig("akka.kafka.consumer")
    val consumerSettings: ConsumerSettings[String, Array[Byte]] =
      ConsumerSettings(kafkaConsumerConfig, new StringDeserializer, new ByteArrayDeserializer)
        .withBootstrapServers(kafkaAddress)
        .withGroupId(kafkaConsumerGroup)

    //Source
    val kafkaSource = Consumer.plainSource(consumerSettings, Subscriptions.topics(kafkaTopic))
    //Run the Stream
    kafkaSource
      .map ( record => BasicLogMessage("Offset:" + record.offset() + " Value:" + record.value.map(_.toChar).mkString).asJson.noSpaces)
      .runForeach( log.info )

  }

}
