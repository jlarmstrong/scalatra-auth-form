organization := "com.example"

name := "scalatra-auth-form"

version := "1.0"

scalaVersion := "2.9.0-1"

seq(webSettings :_*)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.0.0-SNAPSHOT",
  "org.scalatra" %% "scalatra-scalate" % "2.0.0-SNAPSHOT",
  "org.scalatra" %% "scalatra-auth" % "2.0.0-SNAPSHOT",
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "net.liftweb" %% "lift-json" % "2.4-M3",
  "net.liftweb" %% "lift-mongodb-record" % "2.4-M3",
  "org.slf4j" % "slf4j-simple" % "1.6.1" % "runtime"
)

resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"