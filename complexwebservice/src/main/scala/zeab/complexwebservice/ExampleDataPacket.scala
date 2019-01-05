package zeab.complexwebservice

//Imports
import java.util.UUID

case class ExampleDataPacket(msg: String, id:String = UUID.randomUUID.toString)
