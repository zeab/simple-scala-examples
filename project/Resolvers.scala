
//Imports
import sbt.Keys.resolvers
import sbt._

object Resolvers {

  val zeabArtifactory = "Artifactory" at "http://192.168.1.144:8081/artifactory/ivy-dev-local/"

  val allResolvers: Seq[Def.Setting[_]] = Seq(resolvers += zeabArtifactory)

}
