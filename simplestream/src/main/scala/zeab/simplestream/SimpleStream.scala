package zeab.simplestream

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.Logging
//Akka
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.{Done, NotUsed}
//Scala
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object SimpleStream extends Logging {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("SimpleStream", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //HOw many messages to send in one go
    val howMany = 1
    //how often to send messages
    val howOften = 1.second

    val inputSource: Source[Msg, NotUsed] =
      Source
        .repeat(new Msg)
        .throttle(howMany, howOften)

    //Stream Transform/Update
    val transformFlow =
      Flow[Msg]
        .map {"Ahoy! " + _.msg}
        .async

    //Stream output
    val outputSink: Sink[String, Future[Done]] =
      Sink
        .foreach { message => log.info(s"$message") }

    inputSource
      .viaMat(transformFlow)(Keep.both)
      .toMat(outputSink)(Keep.both)
      .run()

  }

}
