package zeab.backpressuredstream

//Imports
import akka.NotUsed
import akka.actor.{ActorRef, Props}
import akka.stream.scaladsl.{Keep, Sink}
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.backpressuredstream.BackPressuredMessages.{Ack, Complete, Init}
import zeab.logging.Logging
//Java
import java.util.concurrent.atomic.LongAdder
//Akka
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.Source
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object BackPressuredStream extends Logging{

  def main(args: Array[String]): Unit = {

    //Akka
    implicit val system:ActorSystem = ActorSystem("BackPressuredStream", AkkaConfigBuilder.buildConfig())
    implicit val mat: ActorMaterializer = ActorMaterializer()
    implicit val ec: ExecutionContext = system.dispatcher

    //Int used in the message
    val counter: LongAdder = new LongAdder()

    //Input Stream
    val inputSource: Source[LongAdder, NotUsed] =
      Source
        .repeat(counter)
        .buffer(100, OverflowStrategy.backpressure)

    //Output stream
    val outputActor: ActorRef = system.actorOf(Props[BackPressuredActor])
    val failed = (ex: Throwable) => BackPressuredMessages.StreamFailure(ex)
    val outputSink: Sink[Any, NotUsed] = Sink.actorRefWithAck(
      outputActor,
      Init,
      Ack,
      Complete,
      failed
    )

    //Run the stream
    inputSource
      .toMat(outputSink)(Keep.both)
      .run()

  }

}
