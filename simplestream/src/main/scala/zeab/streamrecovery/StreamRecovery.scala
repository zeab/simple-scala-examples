package zeab.streamrecovery

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.Logging
//Akka
import akka.stream.{ActorAttributes, Supervision}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.{Done, NotUsed}
//Scala
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object StreamRecovery extends Logging {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("StreamRecovery", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Stream Supervisor
    val decider: Supervision.Decider = {
      case _: ArithmeticException => Supervision.Resume
      case _: Throwable => Supervision.Resume
      case _ => Supervision.stop
    }

    //Stream Input
    val inputSource: Source[Msg, NotUsed] =
      Source
        .repeat(new Msg)
        .throttle(1, 1.second)

    //Stream output
    val outputSink: Sink[Msg, Future[Done]] =
      Sink
        .foreach { message => log.info(s"${message.msg}") }

    //Put it all together and actually run the source
    inputSource
      .toMat(outputSink)(Keep.both).withAttributes(ActorAttributes.supervisionStrategy(decider))
      .run()

  }

}
