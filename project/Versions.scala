
import Common.buildTime

//Versions for all the modules
object Versions {

  //Akka-Stream
  val simpleStreamVersion: String = s"0.0.$buildTime"

  //Http/Udp
  val simpleWebServiceVersion: String = s"0.0.$buildTime"
  val simpleUdpClientVersion: String = s"0.0.$buildTime"
  val simpleUdpServiceVersion: String = s"0.0.$buildTime"
  val complexWebServiceVersion: String = s"0.0.$buildTime"

  //Kafka
  val simpleKafkaProducerVersion: String = s"0.0.$buildTime"
  val simpleKafkaConsumerVersion: String = s"0.0.$buildTime"
  val simpleKafkaEnricherVersion: String = s"0.0.$buildTime"

  //General Scala Examples
  val coreScalaExamplesVersion: String = s"0.0.$buildTime"

  //Slack
  val slackWebhookVersion: String = s"0.0.$buildTime"
  val slackBotVersion: String = s"0.0.$buildTime"

}
