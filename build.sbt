name := "tickets"

version := "0.1"

scalaVersion := "2.12.6"
val akkaVersion = "2.5.25"

// akka
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
// test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit"    % akkaVersion   % "test"
libraryDependencies += "org.scalatest"     %% "scalatest"       % "3.0.5"       % "test"