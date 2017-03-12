
name := "basic-project"

organization := "example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.2"

routesGenerator := InjectedRoutesGenerator

crossScalaVersions := Seq("2.10.4", "2.11.2")

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test",
  "org.postgresql" % "postgresql" % "9.4.1212",
  jdbc,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.2.8.Final"
)

initialCommands := "import example._"
