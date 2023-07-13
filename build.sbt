ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.10"

lazy val root = (project in file("."))
  .settings(
    name := "FxMatcherEngine"
  )

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.12.10" % "provided",
  "org.apache.spark" %% "spark-core" % "3.1.3"  % "provided",
  "org.apache.spark" %% "spark-sql" % "3.1.3"  % "provided",
  "org.apache.spark" %% "spark-hive" % "3.1.3"  % "provided",
  "org.apache.spark" %% "spark-avro" % "3.1.3"  % "provided"

)

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.2"