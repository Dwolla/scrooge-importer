package com.dwolla.sbt.thrift.consume

import com.dwolla.sbt.thrift.consume.ThriftClientsPlugin.autoImport.ThriftClients
import com.twitter.scrooge.ScroogeSBT
import sbt.*
import sbt.Keys.*
import scalafix.sbt.ScalafixPlugin
import scalafix.sbt.ScalafixPlugin.autoImport._
object AddTaglessPlugin extends sbt.AutoPlugin {

  override def trigger = AllRequirements

  override def requires = ScalafixPlugin && ScroogeSBT && ThriftClientsPlugin

  lazy val scalafixBuildSettings = Seq(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value),
    scalafixDependencies += "com.dwolla" %% "finagle-tagless-scalafix" % "0.3.0",
    ThriftClients / libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.9.0",
      "org.typelevel" %% "cats-tagless-macros" % "0.14.0",
      "com.dwolla" %% "async-utils-finagle" % "0.3.0"
    )
  )

  lazy val scalafixProjectSettings =
    Seq(
      Compile / scalafix / unmanagedSources := (Compile / sources).value,
      Compile / compile := Def.taskDyn {
        val compileOutput = (Compile / compile).value

        Def.task {
          (Compile / scalafix).toTask(" AddCatsTaglessInstances").value
          compileOutput
        }
      }.value
    )

  lazy val scroogeProjectSettings =
    Seq(
      scalacOptions ~= (_.filterNot(s =>
        s.startsWith("-Ywarn") || s.startsWith("-Xlint") || s.startsWith("-W") || s.equals(
          "-Xfatal-warnings"
        )
      ))
    )

  override def buildSettings: Seq[Def.Setting[_]] =
    super.buildSettings ++ scalafixBuildSettings

  override def projectSettings: Seq[Def.Setting[_]] =
    super.projectSettings ++ scroogeProjectSettings ++ scalafixProjectSettings

}
