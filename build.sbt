lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  libraryDependencies += cache
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "play-di"
  ).aggregate(guice, cake, macwire, manual, reader)

lazy val guice = (project in file("guice")).
  settings(commonSettings: _*).
  settings(
    name := "play-guice"
  ).enablePlugins(PlayScala)

lazy val cake = (project in file("cake")).
  settings(commonSettings: _*).
  settings(
    name := "play-cake"
  ).enablePlugins(PlayScala)

lazy val macwire = (project in file("macwire")).
  settings(commonSettings: _*).
  settings(
    name := "play-macwire",
    libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.2.2" % "provided"
  ).enablePlugins(PlayScala)

lazy val manual = (project in file("manual")).
  settings(commonSettings: _*).
  settings(
    name := "play-manual"
  ).enablePlugins(PlayScala)

lazy val reader = (project in file("reader")).
  settings(commonSettings: _*).
  settings(
    name := "play-reader",
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.2"
  ).enablePlugins(PlayScala)
