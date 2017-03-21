name := "Events Manager"

version := "1.0.0"

scalaVersion := "2.12.1"

val akka = "2.4.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akka
)