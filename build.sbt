name := """Giga LMS"""
organization := "com.giga"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "org.hsqldb" % "hsqldb" % "2.4.1"