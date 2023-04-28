lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.8",
    name := "test",
    libraryDependencies ++={
      val finagleV = com.twitter.BuildInfo.version
      Seq(
        "com.twitter" %% "finagle-thrift" % finagleV,
        "com.twitter" %% "util-core" % finagleV,
        "com.twitter" %% "scrooge-core" % finagleV
      )
    },
    ThriftClients / libraryDependencies += "javax.annotation" % "javax.annotation-api" % "1.3.2", //necessary for using Java9+ with old finagle
    ThriftClients / thriftDependencies += "com.dwolla.test" % "thrift-publish-test" % "0.1.0",
    TaskKey[Unit]("check") := {
      val version = com.twitter.BuildInfo.version
      if(version != "19.4.0") sys.error(s"Plugin version was overriden to $version")
      val generated = (ThriftClients / Compile / scroogeGen).value
      streams.value.log.info(s"Scrooge deps: $generated")
      if (generated.toList.nonEmpty) {}
      else sys.error("Plugin did not add dependencies to scrooge")
    }
  ).dependsOn(ThriftClients)