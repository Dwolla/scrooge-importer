sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.dwolla" % "scrooge-importer-tagless" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
dependencyOverrides += {
  val dependency = "ch.epfl.scala" % "sbt-scalafix" % "0.10.2"
  val sbtV = (pluginCrossBuild / sbtBinaryVersion).value
  val scalaV = (update / scalaBinaryVersion).value
  sbt.Defaults.sbtPluginExtra(dependency, sbtV, scalaV)
}
resolvers += Resolver.mavenLocal