
//Imports
import sbt.Keys.resolvers
import sbt._

object Resolvers {

  val zeabArtifactoryRemote = "Artifactory" at "http://67.185.79.236:8081/artifactory/ivy-dev-local/"

  //TODO There has got to be a better way to do this than the way I just put it together
  val allResolvers: Seq[Def.Setting[_]] = Seq(resolvers += zeabArtifactoryRemote)

}
