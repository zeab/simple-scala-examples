package zeab.SimpleKafkaProducer

//Imports
import java.util.UUID

class Msg {

  def msg: String = s"Ahoy! ${UUID.randomUUID}"

}
