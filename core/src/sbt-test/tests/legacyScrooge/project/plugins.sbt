sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.dwolla.sbt" % "scrooge-importer" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
dependencyOverrides += {
  val sbtV = (pluginCrossBuild / sbtBinaryVersion).value
  val scalaV = (update / scalaBinaryVersion).value
  sbt.Defaults.sbtPluginExtra("com.twitter" % "scrooge-sbt-plugin" % "19.4.0" , sbtV, scalaV)
}
resolvers += Resolver.mavenLocal