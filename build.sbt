ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "data-pipeline"
  )

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-clients" % "3.0.0",
  "org.apache.hadoop" % "hadoop-client" % "3.3.1",
  "org.apache.spark" %% "spark-core" % "3.5.3",
  "org.apache.spark" %% "spark-sql" % "3.5.3" % "provided",
  "org.apache.spark" %% "spark-streaming" % "3.5.3",
  "org.apache.spark" %% "spark-mllib" % "3.5.3",
  "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.5.3",
)