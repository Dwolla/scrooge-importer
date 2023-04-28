sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.dwolla.sbt" % "scrooge-importer-tagless" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
addSbtPlugin("com.twitter" % "scrooge-sbt-plugin" % "22.7.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.4")

resolvers += Resolver.mavenLocal