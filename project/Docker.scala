
//Imports
import Common._
import Versions._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import sbt.Def
import ModuleNames._

object Docker {

  val repo: Option[String] = Some("zeab")

  //Image List
  val I = new {
    val openjdk8Alpine: String = "openjdk:8-jdk-alpine"
    val openjdk8Slim: String = "openjdk:8-jdk-slim"
  }

  //Base
  val baseDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerUpdateLatest := true
  )

  val simpleWebServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(simpleWebServiceKey, simpleWebServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val complexWebServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(complexWebServiceKey, complexWebServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val simpleKafkaProducerDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(simpleKafkaProducerKey, simpleKafkaProducerVersion, buildTime),
    dockerUpdateLatest := true
  )

  val simpleKafkaConsumerDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(simpleKafkaConsumerKey, simpleKafkaConsumerVersion, buildTime),
    dockerUpdateLatest := true
  )

  val simpleKafkaEnricherDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(simpleKafkaEnricherKey, simpleKafkaEnricherVersion, buildTime),
    dockerUpdateLatest := true
  )

  val simpleUdpClientDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(simpleUdpClientKey, simpleUdpClientVersion, buildTime),
    dockerUpdateLatest := true
  )

  val simpleUdpServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(simpleUdpServiceKey, simpleUdpServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val slackWebhookDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(slackWebhookKey, slackWebhookVersion, buildTime),
    dockerUpdateLatest := true
  )

  val slackBotDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(slackBotKey, slackBotVersion, buildTime),
    dockerUpdateLatest := true
  )
  
}
