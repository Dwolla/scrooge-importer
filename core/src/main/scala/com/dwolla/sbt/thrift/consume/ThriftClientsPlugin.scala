package com.dwolla.sbt.thrift.consume

import com.dwolla.sbt.thrift.consume.ThriftClientsPlugin.autoImport.{ThriftClients, `thrift-clients`, thriftDependencies}
import com.twitter.scrooge.ScroogeSBT
import com.twitter.scrooge.ScroogeSBT.autoImport.*
import sbt.Keys.*
import sbt.{Def, *}

object ThriftClientsPlugin extends sbt.AutoPlugin {

  override def trigger = AllRequirements

  override def requires = ScroogeSBT

  object autoImport {
    val ThriftClients = LocalProject("thrift-clients")

    val thriftDependencies = ThriftClients / settingKey[Seq[ModuleID]]("Thrift dependencies to be added to scrooge")

    val `thrift-clients` = (project in file("thrift-clients"))
      .settings(
        scalacOptions += "-Wconf:cat=unused&src=src_managed/.*:s",
        scalacOptions += "-Wconf:cat=deprecation&src=src_managed/.*:s",
        libraryDependencies ++= (ThriftClients / thriftDependencies).value.map(_ % "thrift"),
        Compile / scroogeThriftDependencies ++= (ThriftClients / thriftDependencies).value.map(_.name),
        Compile / scroogeThriftSources ++= {
          (Compile / scroogeThriftIncludes).value.flatMap { d =>
            (d ** "*.thrift").get
          }
        },
        Compile / scroogeBuildOptions += com.twitter.scrooge.backend.WithFinagle,
        libraryDependencies ++= Seq(
          "com.twitter" %% "finagle-thrift" % com.twitter.BuildInfo.version
        )
      )
  }

  lazy val scroogeProjectSettings =
    Seq(
      scalacOptions ~= (_.filterNot(s =>
        s.startsWith("-Ywarn") || s.startsWith("-Xlint") || s.startsWith("-W") || s.equals(
          "-Xfatal-warnings"
        )
      ))
    )

  override def projectSettings: Seq[Def.Setting[_]] = super.projectSettings ++ scroogeProjectSettings

  override def extraProjects: Seq[Project] = Seq(`thrift-clients`)

  override def buildSettings: Seq[Def.Setting[_]] = ThriftClients / thriftDependencies := Nil
}