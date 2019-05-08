
//Imports
import Common.seqBaseProjectTemplate
import Versions._
import sbt.Def

//Settings
object Settings {

  val coreScalaExamplesSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(coreScalaExamplesVersion)

  val simpleStreamSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleStreamVersion)

  val complexWebServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(complexWebServiceVersion)
  
  val simpleWebServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleWebServiceVersion)
  val simpleUdpClientSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleUdpClientVersion)
  val simpleUdpServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleUdpServiceVersion)

  val simpleKafkaProducerSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleKafkaProducerVersion)
  val simpleKafkaConsumerSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleKafkaConsumerVersion)
  val simpleKafkaEnricherSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleKafkaEnricherVersion)

  val slackWebhookSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(slackWebhookVersion)
  val slackBotSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(slackBotVersion)

  val simpleCassandraReaderSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleCassandraReaderVersion)
  val simpleCassandraWriterSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleCassandraWriterVersion)
  
}
