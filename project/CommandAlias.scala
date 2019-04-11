
//Imports
import sbt.{Def, addCommandAlias}
import ModuleNames._

object CommandAlias {

  //List all Publish Alias here
  val allPublishAlias: Seq[Def.Setting[_]] = Seq.empty

  //List all Docker Alias here
  val allDockerAlias: Seq[Def.Setting[_]] =
    dockerCommands("sws", simpleWebServiceKey) ++
      dockerCommands(simpleWebServiceKey) ++
      dockerCommands("suc", simpleUdpClientKey) ++
      dockerCommands(simpleUdpClientKey) ++
      dockerCommands("cws", complexWebServiceKey) ++
      dockerCommands(complexWebServiceKey) ++
      dockerCommands("travisci", simpleWebServiceKey, simpleUdpClientKey, simpleUdpServiceKey)

  //List all Assembly Alias here
  val allAssemblyAlias: Seq[Def.Setting[_]] =
    assemblyCommands("sws", simpleWebServiceKey) ++
      assemblyCommands(simpleWebServiceKey)

  //Group all the commands for use in build.sbt
  val allCommandAlias: Seq[Def.Setting[_]] =
    allPublishAlias ++ allDockerAlias ++ allAssemblyAlias

  //Make both Docker Alias
  def dockerCommands(key:String, modules:String*): Seq[Def.Setting[_]] = {
    val publishLocal = "docker:publishLocal"
    val publish = "docker:publish"
    addCommandAlias(s"dpl", s"$publishLocal") ++
      addCommandAlias(s"dp", s"$publish") ++
      addCommandAlias(s"dpl-$key", if (modules.isEmpty) s";$key/$publishLocal" else modules.map(module => s";$module/$publishLocal").mkString) ++
      addCommandAlias(s"dp-$key", if (modules.isEmpty) s";$key/$publish" else modules.map(module => s";$module/$publish").mkString)
  }

  //Make both Publish Alias
  def publishCommands(key:String, modules:String*): Seq[Def.Setting[_]] = {
    val publishLocal = "publishLocal"
    val publish = "publish"
    addCommandAlias(s"pl", s"$publishLocal") ++
      addCommandAlias(s"p", s"$publishLocal") ++
      addCommandAlias(s"pl-$key", if (modules.isEmpty) s";clean;$key/$publishLocal" else modules.map(module => s";$module/$publishLocal").mkString) ++
      addCommandAlias(s"p-$key", if (modules.isEmpty) s";$key/$publish" else modules.map(module => s";$module/$publish").mkString)
  }

  //Make both Publish Alias
  def assemblyCommands(key:String, modules:String*): Seq[Def.Setting[_]] = {
    val assembly = "assembly"
    addCommandAlias(s"a-$key", if (modules.isEmpty) s";$key/$assembly" else modules.map(module => s";$module/$assembly").mkString)
  }

}
