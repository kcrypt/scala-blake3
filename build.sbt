import sbt.Keys.crossScalaVersions
import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scala210 = "2.10.7"
lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.14"
lazy val scala213 = "2.13.6"
lazy val scala3 = "3.0.1"

lazy val scalatestVersion = "3.2.9"

lazy val blake3jniVersion = "0.2.2"

name := "blake3"
ThisBuild / organization := "ky.korins"

ThisBuild / dynverSeparator := "-"

ThisBuild / scalaVersion := scala213
ThisBuild / crossScalaVersions := Seq()

ThisBuild / scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-unchecked",
  "-deprecation"
)

ThisBuild / publishTo := sonatypePublishToBundle.value

headerLicense := LicenseDefinition.template

lazy val blake3 = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(AutomateHeaderPlugin)
  .in(file("."))
  .settings(
    publish / skip := false,
    Test / publishArtifact := false,
    buildInfoKeys := Seq(
      BuildInfoKey.action("commit") {
        scala.sys.process.Process("git rev-parse HEAD").!!.trim
      }
    ),
    headerLicense := LicenseDefinition.template,
    buildInfoPackage := "ky.korins.blake3",
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % scalatestVersion % Test
    )
  )
  .jvmSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala210, scala211, scala212, scala213, scala3)
  )
  .jsSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala211, scala212, scala213, scala3)
  )
  .nativeSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala211, scala212, scala213),
    nativeLinkStubs := true
  )

lazy val bench = project
  .in(file("bench"))
  .dependsOn(blake3.jvm)
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    name := "blake3-bench",
    publish / skip := true,
    assembly / assemblyJarName := "bench.jar",
    assembly / mainClass := Some("org.openjdk.jmh.Main"),
    assembly / test := {},
    libraryDependencies ++= Seq(
      "io.lktk" % "blake3jni" % blake3jniVersion
    ),
    headerLicense := LicenseDefinition.template,
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "MANIFEST.MF") =>
        MergeStrategy.discard
      case _ =>
        MergeStrategy.first
    },
    Jmh / assembly := (Jmh / assembly).dependsOn(Jmh / Keys.compile).value
  )
  .enablePlugins(JmhPlugin)
