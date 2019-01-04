package zeab.actorassourceforroute

import akka.actor.ActorRef
import akka.stream.stage.GraphStageLogic.StageActor
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler, StageLogging}
import akka.stream.{Attributes, Outlet, SourceShape}

import scala.collection.immutable.Queue

class MessageSource(sourceFeeder: ActorRef) extends GraphStage[SourceShape[StreamDataPacket]] {
  val out: Outlet[StreamDataPacket] = Outlet("MessageSource")
  override val shape: SourceShape[StreamDataPacket] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      lazy val self: StageActor = getStageActor(onMessage)
      var messages: Queue[StreamDataPacket] = Queue()

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          log.info("onPull() called...")
          pump()
        }
      })

      private def pump(): Unit = {
        if (isAvailable(out) && messages.nonEmpty) {
          log.info("ready to dequeue")
          messages.dequeue match {
            case (msg: StreamDataPacket, newQueue: Queue[StreamDataPacket]) =>
              log.info("got message from queue, pushing: {} ", msg)
              push(out, msg)
              messages = newQueue
          }
        }
      }

      override def preStart(): Unit = {
        log.info("pre-starting stage, assigning StageActor to sourceFeeder")
        sourceFeeder ! AssignStageActor(self.ref)
      }

      private def onMessage(x: (ActorRef, Any)): Unit =
      {
        x match {
          case (_, msg: StreamDataPacket) =>
            log.info("received msg, queueing: {} ", msg)
            messages = messages.enqueue(msg)
            pump()
          case _ => log.error("Something bad happened inside the onMessage")
        }
      }
    }
}
