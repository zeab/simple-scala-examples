package zeab.backpressuredstream

object BackPressuredMessages {

  final case class StreamFailure(ex: Throwable)

  case object Init

  case object Ack

  case object Complete

}
