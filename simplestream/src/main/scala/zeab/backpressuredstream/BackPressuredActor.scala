package zeab.backpressuredstream

//Imports
import java.util.concurrent.atomic.LongAdder

import akka.actor.Actor
import akka.event.{Logging, LoggingAdapter}
import zeab.backpressuredstream.BackPressuredMessages.{Ack, Init}

class BackPressuredActor extends Actor{

  //Actor Settings
  val actorLog: LoggingAdapter = Logging(context.system, this)

  def receive: Receive = count()

  def count(c: Int = 0): Receive = {
    case Init =>
      sender ! Ack
    case m: LongAdder =>
      sender ! Ack
      m.increment()
      actorLog.info(s"$m")
  }

}
