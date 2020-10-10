import sbt.Keys.crossScalaVersions
import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.12"
lazy val scala213 = "2.13.3"

lazy val dotty = "0.27.0-RC1"

lazy val scalatestVersion = "3.2.2"

name := "blake3"
organization in ThisBuild := "ky.korins"
version in ThisBuild := "1.6.1"

scalaVersion in ThisBuild := dotty
crossScalaVersions in ThisBuild := Seq()

scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.8",
  "-unchecked",
  "-deprecation"
)

publishTo in ThisBuild := sonatypePublishToBundle.value

lazy val blake3 = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(disableDottyDocs)
  .settings(
    skip in publish := false,
    publishArtifact in Test := false,
    buildInfoKeys := Seq(
      BuildInfoKey.action("commit") {
        scala.sys.process.Process("git rev-parse HEAD").!!.trim
      }
    ),
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % scalatestVersion % Test,
    )
  )
  .jvmSettings(
    scalaVersion := dotty,
    crossScalaVersions := Seq(scala212, scala211, scala213, dotty)
  )
  .jsSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala211, scala212, scala213),
  )
  .nativeSettings(
    scalaVersion := scala211,
    crossScalaVersions := Seq(scala211),
    nativeLinkStubs := true
  )

lazy val bench = project.in(file("bench"))
  .dependsOn(blake3.jvm)
  .settings(disableDottyDocs)
  .settings(
    name := "blake3-bench",
    skip in publish := true
  )
  .enablePlugins(JmhPlugin)

// Dotty has at least two bugs in docs generation:
//  - it copies whole project to _site
//  - it creates empty javadocs artifact.
// Details: https://github.com/lampepfl/dotty/issues/8769
// Let disable it
lazy val disableDottyDocs = Seq(
  sources in (Compile, doc) := {
    if (isDotty.value) Seq() else (sources in (Compile, doc)).value
  }
)