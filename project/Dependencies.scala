//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val datastax                    = "3.4.0"
    val wabonki                     = "2.0.22"
  }

  //List of Dependencies
  val D = new {
    //Wabonki
    val wabonki                     = "zeab" %% "wabonki" % V.wabonki
    val datastax                    = "com.datastax.cassandra" % "cassandra-driver-extras" % V.datastax
  }

  val commonDependencies: Seq[ModuleID] = Seq(
    D.wabonki,
    D.datastax
  )

}
