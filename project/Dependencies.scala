//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val wabonki                     = "0.0.6"
  }

  //List of Dependencies
  val D = new {
    //Wabonki
    val wabonki                     = "zeab" %% "wabonki" % V.wabonki
  }

  val commonDependencies: Seq[ModuleID] = Seq(
    D.wabonki
  )

}
