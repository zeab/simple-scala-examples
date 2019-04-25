package slackwebhook

//Imports
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.akkatools.slackbot.slackwebhook.SlackWebhookActor

import scala.concurrent.ExecutionContext

object SlackWebhook {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system: ActorSystem = ActorSystem("SlackWebhook", AkkaConfigBuilder.buildConfig())
    implicit val ec: ExecutionContext = system.dispatcher

    system.actorOf(Props(classOf[SlackWebhookActor]))

  }

}
