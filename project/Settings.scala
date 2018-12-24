
//Imports
import Common.seqBaseProjectTemplate
import Versions._
import sbt.Def

//Settings
object Settings {

  val simpleStreamSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleStreamVersion)

  val simpleWebServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleWebServiceVersion)
  val simpleUdpClientSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleUdpClientVersion)
  val simpleUdpServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleUdpServiceVersion)

  val simpleKafkaProducerSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleKafkaProducerVersion)
  val simpleKafkaConsumerSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleKafkaConsumerVersion)
  val simpleKafkaEnricherSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleKafkaEnricherVersion)

}
