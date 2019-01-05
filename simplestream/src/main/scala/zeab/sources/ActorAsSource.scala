package zeab.sources

import akka.actor.ActorRef
import akka.stream.{OverflowStrategy, ThrottleMode}
import akka.stream.scaladsl.Source
import akka.util.ByteString

import scala.concurrent.duration._

object ActorAsSource {

  def main(args: Array[String]): Unit = {

    val source: Source[ByteString, ActorRef] = Source.actorRef(10, OverflowStrategy.dropTail)
      .throttle(elements = 1000, per = 1.second, maximumBurst = 1, mode = ThrottleMode.Shaping)
      .map(_.toString)
      .map(s => ByteString(s + "\n"))
    
  }

}
