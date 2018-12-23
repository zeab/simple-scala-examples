package zeab.simplestream

//Imports
import zeab.akkatools.akkaconfigbuilder.AkkaConfigBuilder
import zeab.logging.Logging
//Java
import java.util.concurrent.atomic.LongAdder
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

    //Int used in the message
    val counter: LongAdder = new LongAdder()

    val quantityOfElements: Int = 5

    //Stream input
    val inputSource: Source[LongAdder, NotUsed] =
      Source
        .repeat(counter)
        .throttle(quantityOfElements, 5.seconds)

    //Stream Transform/Update
    val transformFlow = Flow[LongAdder].map { counter =>
      counter.increment()
      counter
    }.async

    //Stream output
    val outputSink: Sink[LongAdder, Future[Done]] =
      Sink
        .foreach { message => log.info(s"${message.intValue}") }

    //Run the stream
    inputSource
      .viaMat(transformFlow)(Keep.both)
      .toMat(outputSink)(Keep.both)
      .run()

  }

}
