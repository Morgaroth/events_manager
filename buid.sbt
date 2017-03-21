name := "Events Manager"

version := "1.0.0"

scalaVersion := "2.12.1"

val akka = "2.4.17"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akka,
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8"
)