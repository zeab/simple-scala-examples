//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val wabonki                     = "0.0.27"
  }

  //List of Dependencies
  val D = new {
    //Wabonki
    val wabonki                     = "zeab" %% "wabonki" % V.wabonki
    val classUtil                   = "org.clapper" %% "classutil" % "1.4.0"
  }

  val commonDependencies: Seq[ModuleID] = Seq(
    D.wabonki,
    D.classUtil
  )

}
