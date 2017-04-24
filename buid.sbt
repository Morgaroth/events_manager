name := "Events Manager"

version := "1.0.0"

scalaVersion := "2.12.1"

val akka = "2.4.17"
val slick = "3.2.0"
val slickPG = "0.15.0-RC"
val pgJDBC = "42.0.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akka,
  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "joda-time" % "joda-time" % "2.9.7",
  "org.joda" % "joda-convert" % "1.8",
  "com.typesafe.slick" %% "slick" % slick,
  "com.typesafe.slick" %% "slick-hikaricp" % slick exclude("com.zaxxer", "HikariCP-java6"),
  //  "com.zaxxer" % "HikariCP" % "2.5.1",
  "com.github.tminglei" %% "slick-pg" % slickPG,
  "com.github.tminglei" %% "slick-pg_joda-time" % slickPG,
  "org.postgresql" % "postgresql" % pgJDBC,
  "com.typesafe.slick" %% "slick-codegen" % slick,
  "org.flywaydb" % "flyway-core" % "4.0.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)

enablePlugins(JavaAppPackaging)
