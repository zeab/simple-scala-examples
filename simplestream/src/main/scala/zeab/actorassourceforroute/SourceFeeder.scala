package zeab.actorassourceforroute

import akka.actor.{Actor, ActorRef, Stash}
import zeab.logging.Logging

class SourceFeeder extends Actor with Stash with Logging{

  def receive: Receive = {
    case _: String => stash()
    case AssignStageActor(stageActor: ActorRef) =>
      unstashAll()
      context.become(receiveNew(stageActor))
  }

  def receiveNew(stageActor: ActorRef): Receive = {
    case msg: String =>
      log.info("sourceFeeder received message, forwarding to stage: {} ", msg)
      stageActor ! msg
  }

  //Subscribe to the event bus
  //Lifecycle Hooks
  /** Log Name on Start */
  override def preStart: Unit = {
    context.system.eventStream.subscribe(self, classOf[String])
  }
  /** Log Name on Stop */
  override def postStop: Unit = {
    context.system.eventStream.unsubscribe(self, classOf[String])
  }

}
