package zeab.backpressuredstream

//Imports
import zeab.backpressuredstream.BackPressuredMessages.{Ack, Complete, Init, StreamFailure}
//Java
import java.util.concurrent.atomic.LongAdder
//Akka
import akka.actor.Actor
import akka.event.{Logging, LoggingAdapter}

class BackPressuredActor extends Actor{

  //Actor Settings
  val actorLog: LoggingAdapter = Logging(context.system, this)

  //Receive
  def receive: Receive = count()

  //Behaviors
  def count(c: Int = 0): Receive = {
    case Init => sender ! Ack
    case Complete => actorLog.info("Back Pressured Completed")
    case StreamFailure(ex) => actorLog.error(ex.toString)
    case m: LongAdder =>
      sender ! Ack
      m.increment()
      actorLog.info(s"$m")
  }

}
