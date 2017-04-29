
name := "basic-project"

organization := "example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

crossScalaVersions := Seq("2.10.4", "2.11.7")

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies ++= Seq(
  "org.slf4j"               % "slf4j-log4j12"    % "1.7.5",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "org.postgresql" % "postgresql" % "9.4.1212",
  jdbc,
  javaJpa,
  cache,
  javaWs,
  "be.objectify" % "deadbolt-java_2.11" % "2.5.4",
  "be.objectify" % "deadbolt-core_2.11" % "2.4.3",
"org.hibernate" % "hibernate-entitymanager" % "5.2.8.Final"
)

initialCommands := "import example._"
