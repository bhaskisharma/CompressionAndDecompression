name := "AgodaZipper"

version := "0.1"

scalaVersion := "2.13.3"


libraryDependencies ++= {
  Seq(
    "org.scala-lang" % "scala-library" % "2.13.3",
    "org.apache.commons" % "commons-io" % "1.3.2",
    "org.scalatestplus" %% "junit-4-13" % "3.2.2.0" % "test"
  )
}