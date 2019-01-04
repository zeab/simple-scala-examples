
//Imports
import sbt.Keys.resolvers
import sbt._

object Resolvers {

  val zeabArtifactoryLocal = "Artifactory" at "http://192.168.1.144:8081/artifactory/ivy-dev-local/"
  val zeabArtifactoryRemote = "Artifactory" at "http://67.185.79.236:8081/artifactory/ivy-dev-local/"

  //TODO There has got to be a better way to do this than the way I just put it together
  val allResolvers: Seq[Def.Setting[_]] = Seq(resolvers += zeabArtifactoryLocal) ++ Seq(resolvers += zeabArtifactoryRemote)

}
