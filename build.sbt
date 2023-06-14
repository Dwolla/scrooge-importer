ThisBuild / organizationName := "Dwolla"
ThisBuild / organization := "com.dwolla"
ThisBuild / startYear := Some(2023)
ThisBuild / homepage := Some(url("https://github.com/Dwolla/scrooge-importer"))
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  Developer(
    "bpholt",
    "Brian Holt",
    "bholt+scrooge-importer@dwolla.com",
    url("https://dwolla.com")
  ),
  Developer(
    "hparker",
    "Henry Parker",
    "hparker+scrooge-importer@dwolla.com",
    url("https://dwolla.com")
  )
)
ThisBuild / tlBaseVersion := "0.2"
ThisBuild / tlCiReleaseBranches := Seq("main")
ThisBuild / tlSonatypeUseLegacyHost := true
ThisBuild / sbtPlugin := true

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  scriptedLaunchOpts := { scriptedLaunchOpts.value ++
    Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
  },
  scriptedBufferLog := false
)

lazy val root = (project in file("."))
  .settings(
    name:= "scrooge-importer",
    scripted := (scripted dependsOn (publisher / scripted).toTask("")).evaluated
  )
  .aggregate(core, tagless)
  .enablePlugins(NoPublishPlugin)
  .enablePlugins(SbtPlugin)

lazy val core = (project in file("core"))
  .settings(
    name := "scrooge-importer",
    testSettings,
    addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % "22.7.0")
  )
  .enablePlugins(SbtPlugin)

lazy val tagless = (project in file("tagless"))
  .settings(
    name := "scrooge-importer-tagless",
    testSettings,
    addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % "22.7.0"),
    addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.4")
  )
  .dependsOn(core)
  .enablePlugins(SbtPlugin)

lazy val publisher = (project in file("publisher"))
  .settings(testSettings)
  .enablePlugins(NoPublishPlugin)
  .enablePlugins(SbtPlugin)