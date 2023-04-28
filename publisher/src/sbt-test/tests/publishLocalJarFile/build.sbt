lazy val root = (project in file("."))
  .settings(
    version := "0.1.0",
    scalaVersion := "2.12.8",
    name := "thrift-publish-test",
    organization := "com.dwolla.test",
    crossPaths := false,
    Compile / unmanagedResourceDirectories += baseDirectory.value / "thrift",
    TaskKey[Unit]("check") := {
      val res = (Compile / unmanagedResources).value.map(_.name).exists(_.contains("thrift"))
      if (res) {} else sys.error("File not added to resources")
    }
  )