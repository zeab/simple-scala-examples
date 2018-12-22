package zeab.simplewebservice

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.Logging
//Akka
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
//Scala
import scala.concurrent.ExecutionContext

object SimpleWebService extends Logging{

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val actorSystem:ActorSystem = ActorSystem("QuireAgent", AkkaConfigBuilder.buildConfig())
    implicit val actorMaterializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = actorSystem.dispatcher







  }

}
