
//Imports
import Common.seqBaseProjectTemplate
import Versions._
import sbt.Def

//Settings
object Settings {

  val simpleWebServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleWebServiceVersion)
  val simpleUdpClientSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(simpleUdpClientVersion)

}
