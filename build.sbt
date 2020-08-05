enablePlugins(JavaAppPackaging, AshScriptPlugin)
name := "Microservices"

version := "0.1"

scalaVersion := "2.12.12"

dockerBaseImage := "openjdk:8-jre"
packageName := "recommender"

val circeVersion = "0.12.3"
val doobieVersion = "0.8.8"

libraryDependencies ++= Seq(

  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-actor" % "2.6.8",

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",

  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-mllib" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",

  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2"   % doobieVersion
)