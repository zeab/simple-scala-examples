
//Imports
import Common.disablePublishing
import Settings._
import Dependencies._
import Docker._
import ModuleNames._
import Resolvers.allResolvers

//Add all the command alias's
CommandAlias.allCommandAlias

lazy val corescalaexamples = (project in file(coreScalaExamplesKey))
  .settings(coreScalaExamplesSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)

lazy val simplewebservice = (project in file(simpleWebServiceKey))
  .settings(simpleWebServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleWebServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)
  .settings(mappings in Universal += file("./simplewebservice/webUi/index.html") -> "/webUi/index.html")
  .settings(disablePublishing: _*)

lazy val simpleudpclient = (project in file(simpleUdpClientKey))
  .settings(simpleUdpClientSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleUdpClientDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)
  .settings(disablePublishing: _*)

lazy val simplestream = (project in file(simpleStreamKey))
  .settings(simpleStreamSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)

lazy val simpleudpservice = (project in file(simpleUdpServiceKey))
  .settings(simpleUdpServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleUdpServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)
  .settings(disablePublishing: _*)

lazy val simplekafkaproducer = (project in file(simpleKafkaProducerKey))
  .settings(simpleKafkaProducerSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleKafkaProducerDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)
  .settings(disablePublishing: _*)

lazy val simplekafkaconsumer = (project in file(simpleKafkaConsumerKey))
  .settings(simpleKafkaConsumerSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleKafkaConsumerDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)
  .settings(disablePublishing: _*)

lazy val simplekafkaenricher = (project in file(simpleKafkaEnricherKey))
  .settings(simpleKafkaEnricherSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleKafkaEnricherDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)
  .settings(disablePublishing: _*)

lazy val complexwebservice = (project in file(complexWebServiceKey))
  .settings(complexWebServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(complexWebServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val slackwebhook = (project in file(slackWebhookKey))
  .settings(slackWebhookSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(slackWebhookDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val slackbot = (project in file(slackBotKey))
  .settings(slackBotSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(slackBotDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val simplecassandrareader = (project in file(simpleCassandraReaderKey))
  .settings(simpleCassandraReaderSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)

lazy val simplecassandrawriter = (project in file(simpleCassandraWriterKey))
  .settings(simpleCassandraWriterSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)