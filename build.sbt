name := "netease_music_to_xspf"

version := "0.1"

scalaVersion := "2.13.1"

ThisBuild / organization := "org.fengkx"

mainClass in Compile := Some("org.fengkx.netease_music_to_xspf.Main")

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
