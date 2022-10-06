name := "onair2"
lazy val akkaHttpVersion = "10.2.9"
 
version := "1.0" 
      
lazy val `onair2` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

scalaSource in Test := baseDirectory.value / "tests"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )


val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic"% "0.14.1",
  "io.circe" %% "circe-generic-extras"% "0.14.1",
  "io.circe" %% "circe-parser"% "0.14.1",
  "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,


  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-all" % "1.9.5" % Test,
  "org.scalacheck" %% "scalacheck" % "1.12.6" % Test

)

    
unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      