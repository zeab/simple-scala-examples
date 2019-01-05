package zeab.complexwebservice.webservice.actorassourceforhttpstream

//Imports
import zeab.complexwebservice.ExampleDataPacket
//Akka
import akka.actor.ActorRef
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.GraphStageLogic.StageActor
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler, StageLogging}
//Scala
import scala.collection.immutable.Queue

class ExampleDataPacketGraph(sourceFeeder: ActorRef) extends GraphStage[SourceShape[ExampleDataPacket]] {
  val out: Outlet[ExampleDataPacket] = Outlet("MessageSource")
  override val shape: SourceShape[ExampleDataPacket] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      lazy val self: StageActor = getStageActor(onMessage)
      var messages: Queue[ExampleDataPacket] = Queue()

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          log.debug("onPull() called...")
          pump()
        }
      })

      private def pump(): Unit = {
        if (isAvailable(out) && messages.nonEmpty) {
          log.debug("ready to dequeue")
          messages.dequeue match {
            case (msg: ExampleDataPacket, newQueue: Queue[ExampleDataPacket]) =>
              log.debug("got message from queue, pushing: {} ", msg)
              push(out, msg)
              messages = newQueue
          }
        }
      }

      override def preStart(): Unit = {
        log.debug("pre-starting stage, assigning StageActor to sourceFeeder")
        sourceFeeder ! self.ref
      }

      private def onMessage(x: (ActorRef, Any)): Unit =
      {
        x match {
          case (_, msg: ExampleDataPacket) =>
            log.debug("received msg, queueing: {} ", msg)
            messages = messages.enqueue(msg)
            pump()
          case _ => log.error("We dont take that kind of message in the message source")
        }
      }
    }
}

