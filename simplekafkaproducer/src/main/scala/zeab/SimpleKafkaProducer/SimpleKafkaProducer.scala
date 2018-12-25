package zeab.SimpleKafkaProducer

//Imports
import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.typesafe.config.Config
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.{BasicLogMessage, Logging}

import scala.concurrent.{ExecutionContext, Future}

object SimpleKafkaProducer extends Logging{

  def main(args: Array[String]): Unit = {

    //Settings
    val kafkaAddress = "localhost:9021"
    val kafkaConcurrentCount: Int = envGrok("KAFKA_CONCURRENT_COUNT", "1").toInt
    val kafkaTopic: String = envGrok("KAFKA_TOPIC", "thistopic")

    //Akka
    implicit val actorSystem:ActorSystem = ActorSystem("SimpleKafkaProducer", AkkaConfigBuilder.buildConfig())
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher


    //Kafka Producer Settings
    val kafkaConfig: Config = actorSystem.settings.config.getConfig("akka.kafka.producer")
    val kafkaProducerSettings: ProducerSettings[String, String] =
      ProducerSettings(kafkaConfig, new StringSerializer, new StringSerializer)
        .withBootstrapServers(kafkaAddress)
    val kafkaProducer: KafkaProducer[String,String] = kafkaProducerSettings.createKafkaProducer()

    //Source
    val kafkaSource =
      Source.repeat(new Msg)

    //Sink
    val kafkaSink: Sink[ProducerRecord[String, String], Future[Done]] = Producer.plainSink(kafkaProducerSettings, kafkaProducer)

    for(_ <- 1 to 1){
      //Stream
      kafkaSource
        .mapAsync(100){newMsg =>
          Future{new ProducerRecord[String, String](kafkaTopic, newMsg.msg)}}.async
        .toMat(kafkaSink)(Keep.both)
        .run()
    }

  }

}
