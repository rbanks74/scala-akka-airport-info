name := "scala-akka-airport-info"

version := "1.0"

scalaVersion := "2.11.7"

resolvers in ThisBuild ++= Seq("Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases/")
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"


libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.0",
  "org.reactivemongo" %% "reactivemongo" % "0.11.9",
  "org.mongodb" % "mongodb-driver-async" % "3.2.1",
  "org.mongodb" % "mongodb-driver-core" % "3.0.2",
  "org.mongodb" % "bson" % "3.2.1",
  "org.mongodb.scala" % "mongo-scala-bson_2.11" % "1.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6",
  "com.novus" %% "salat" % "1.9.9",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "io.reactivex" %% "rxscala" % "0.26.0",
  "com.typesafe.play" %% "play-json" % "2.4.4",
  "com.novus" %% "salat" % "1.9.9",
  "org.mongodb" %% "casbah" % "3.1.1",
  "io.argonaut" %% "argonaut" % "6.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.scalaz" %% "scalaz-core" % "7.2.6"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")