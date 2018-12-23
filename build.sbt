
//Imports
import Settings._
import Dependencies._
import Docker._
import ModuleNames._
import Resolvers.allResolvers

//Add all the command alias's
CommandAlias.allCommandAlias

lazy val simplewebservice = (project in file(simpleWebServiceKey))
  .settings(simpleWebServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleWebServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val simpleudpclient = (project in file(simpleUdpClientKey))
  .settings(simpleUdpClientSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)
  .settings(simpleUdpClientDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val simplestream = (project in file(simpleStreamKey))
  .settings(simpleStreamSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .enablePlugins(Artifactory)
  .settings(allResolvers: _*)