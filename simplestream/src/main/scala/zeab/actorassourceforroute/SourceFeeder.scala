package zeab.actorassourceforroute

import akka.actor.{Actor, ActorRef, Stash}
import zeab.logging.Logging

class SourceFeeder extends Actor with Stash with Logging{

  def receive: Receive = {
    case _: String => stash()
    case _: StreamDataPacket => stash()
    case AssignStageActor(stageActor: ActorRef) =>
      unstashAll()
      context.become(receiveNew(stageActor))
  }

  def receiveNew(stageActor: ActorRef): Receive = {
    case msg: String =>
      log.info("sourceFeeder received message, forwarding to stage: {} ", msg)
      stageActor ! msg
    case msg: StreamDataPacket =>
      log.info("sourceFeeder received message, forwarding to stage: {} ", msg)
      stageActor ! msg
  }

  //Lifecycle Hooks
  /** Log Name on Start */
  override def preStart: Unit = {
    context.system.eventStream.subscribe(self, classOf[StreamDataPacket])
  }
  /** Log Name on Stop */
  override def postStop: Unit = {
    context.system.eventStream.unsubscribe(self, classOf[StreamDataPacket])
  }

}
