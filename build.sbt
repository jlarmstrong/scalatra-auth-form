organization := "com.example"

name := "scalatra-auth-form"

version := "1.0.1"

scalaVersion := "2.9.1"

seq(webSettings :_*)

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "2.0.2",
  "org.scalatra" %% "scalatra-scalate" % "2.0.2",
  "org.scalatra" %% "scalatra-auth" % "2.0.2",
  "org.eclipse.jetty" % "jetty-webapp" % "8.0.1.v20110908" % "container",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "org.scalatra" %% "scalatra-lift-json" % "2.0.2",
  "net.liftweb" %% "lift-mongodb-record" % "2.4-M5",
  "org.slf4j" % "slf4j-simple" % "1.6.1" % "runtime"
)

resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

