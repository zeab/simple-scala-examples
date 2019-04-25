package slackwebhook

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.slackbot.slackwebhook.SlackWebhookActor
import zeab.logging.Logging
//Akka
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.ExecutionContext

object SlackWebhook extends Logging {

  def main(args: Array[String]): Unit = {

    //Settings
    val slackDomain: String = getEnvVar[String]("SLACK_DOMAIN", "localhost:9092")
    val slackWebhook: String = getEnvVar[String]("SLACK_WEBHOOK")

    //Akka
    implicit val system: ActorSystem = ActorSystem("SlackBot", AkkaConfigBuilder.buildConfig())
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = system.dispatcher


    system.actorOf(Props(classOf[SlackWebhookActor]))

  }

}
