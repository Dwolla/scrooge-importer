lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.17",
    name := "test",
    ThriftClients / thriftDependencies += "com.dwolla.test" % "thrift-publish-test" % "0.1.0",
    TaskKey[Unit]("check") := {
      val generated = (ThriftClients / Compile / scroogeGen).value
      streams.value.log.info(s"Scrooge deps: $generated")
      if (generated.toList.nonEmpty) {}
      else sys.error("Plugin did not add dependencies to scrooge")
    }
  ).dependsOn(ThriftClients)