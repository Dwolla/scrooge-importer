import _root_.scalafix.sbt.BuildInfo.{scalafixVersion => sfixVer}

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.8", //scalafix 0.10.4 should fail to download dependencies on this version because the semanticdb version that it relies on (4.6.0) is not supported on 2.12.8.
    name := "test",
    ThriftClients / thriftDependencies += "com.dwolla.test" % "thrift-publish-test" % "0.1.0",
    TaskKey[Unit]("check") := {
      if(sfixVer != "0.10.2") sys.error(s"Scalafix version overridden to $sfixVer")
      val generated = (ThriftClients / Compile / scroogeGen).value
      streams.value.log.info(s"Scrooge deps: $generated")
      if (generated.toList.nonEmpty) {}
      else sys.error("Plugin did not add dependencies to scrooge")
    }
  ).dependsOn(ThriftClients)