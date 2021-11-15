name := "Text transformation"

version := "0.1"

scalaVersion := "2.13.6"


val macwire   = "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided"
val tesseract = "net.sourceforge.tess4j" % "tess4j" % "4.0.0"
val scalamock = "org.scalamock"  %% "scalamock" % "5.1.0" % Test
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.9"  % Test


lazy val `LagomService` = (project in file("."))
  .aggregate(`text-transformation-api`, `text-transformation-impl` )

lazy val `text-transformation-api` = (project in file("text-transformation-api"))
  .settings(
    ThisBuild / lagomKafkaEnabled := false,
    ThisBuild / lagomCassandraEnabled := false,
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `text-transformation-impl` = (project in file("text-transformation-impl"))
  .enablePlugins(LagomScala)
  .settings(lagomForkedTestSettings: _*)
  .settings(
    ThisBuild / lagomKafkaEnabled := false,
    ThisBuild / lagomCassandraEnabled := false,
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceJdbc,
      lagomScaladslKafkaBroker,
      lagomScaladslAkkaDiscovery,
      lagomScaladslTestKit,
      tesseract,
      macwire,
      scalamock,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`text-transformation-api`)