package zeab.backpressuredstream

object BackPressuredMessages {

  case object Init
  case object Ack
  case object Complete

  final case class StreamFailure(ex: Throwable)

}
