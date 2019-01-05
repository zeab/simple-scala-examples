package zeab.killswitch

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.Logging
//Akka
import akka.stream.KillSwitches
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.{Done, NotUsed}
//Scala
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object KillSwitch extends Logging {

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("KillSwitch", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    val inputSource: Source[Msg, NotUsed] =
      Source
        .repeat(new Msg)
        .throttle(1, 1.second)

    //Stream output
    val outputSink: Sink[Msg, Future[Done]] =
      Sink
        .foreach { message => log.info(s"${message.msg}") }

    //Put it all together and actually run the source
    val (killSwitch, _) =
      inputSource
        .viaMat(KillSwitches.single)(Keep.right)
        .toMat(outputSink)(Keep.both)
        .run()

    //Terminate the stream after 5 seconds
    system.scheduler.scheduleOnce(5.second){
      killSwitch.shutdown
      log.info("Stream Ended")
      system.terminate
    }

  }

}
