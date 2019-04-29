package slackwebhook

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.slack.slackwebhook.{SlackWebHook2, SlackWebhook}
import zeab.logging.Logging
//Akka
import akka.actor.{ActorRef, ActorSystem, Props}
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
//Java
import java.util.UUID

object SlackWebhook extends Logging {

  def main(args: Array[String]): Unit = {

    //Settings
    val webhook: String = getEnvVar[String]("SLACK_WEBHOOK") match {
      case Right(wh) => wh
      case Left(ex) =>
        log.error(ex.toString)
        System.exit(1)
        ""
    }

    //Akka
    implicit val system: ActorSystem = ActorSystem("SlackBot", AkkaConfigBuilder.buildConfig())
    implicit val executionContext: ExecutionContext = system.dispatcher

    //val slackWebhook: ActorRef = system.actorOf(Props(classOf[SlackWebhook], webhook, 1000))
    val slackWebhook: ActorRef = system.actorOf(Props(classOf[SlackWebHook2]))

    system.scheduler.schedule(0.second, 250.millisecond){
      slackWebhook ! UUID.randomUUID.toString
    }

  }

}
