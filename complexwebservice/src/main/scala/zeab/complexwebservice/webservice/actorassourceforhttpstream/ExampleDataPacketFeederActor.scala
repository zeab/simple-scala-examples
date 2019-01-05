package zeab.complexwebservice.webservice.actorassourceforhttpstream

//Imports
import zeab.complexwebservice.ExampleDataPacket
import zeab.logging.Logging
//Akka
import akka.actor.{Actor, ActorRef, Stash}

//This is the actor that you will be sending to in order to drop items into the stream
class ExampleDataPacketFeederActor extends Actor with Stash with Logging {

  def receive: Receive = {
    case _: ExampleDataPacket => stash()
    case stageActor: ActorRef =>
      unstashAll()
      context.become(receiveNew(stageActor))
  }

  def receiveNew(stageActor: ActorRef): Receive = {
    case m: ExampleDataPacket =>
      log.debug("sourceFeeder received message, forwarding to stage: {} ", m)
      stageActor ! m
  }

  //Lifecycle Hooks
  /** Log Name on Start */
  override def preStart: Unit = {
    context.system.eventStream.subscribe(self, classOf[ExampleDataPacket])
  }

  /** Log Name on Stop */
  override def postStop: Unit = {
    context.system.eventStream.unsubscribe(self, classOf[ExampleDataPacket])
  }

}
