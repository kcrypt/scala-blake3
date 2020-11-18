import sbt.Keys.crossScalaVersions
import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.12"
lazy val scala213 = "2.13.3"
lazy val scala3 = "3.0.0-M1"

lazy val scalatestVersion = "3.2.3"

name := "blake3"
organization in ThisBuild := "ky.korins"
version in ThisBuild := "1.7.0"

scalaVersion in ThisBuild := scala3
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
  .settings(
    skip in publish := false,
    publishArtifact in Test := false,
    buildInfoKeys := Seq(
      BuildInfoKey.action("commit") {
        scala.sys.process.Process("git rev-parse HEAD").!!.trim
      }
    ),
    buildInfoPackage := "ky.korins.blake3",
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % scalatestVersion % Test,
    )
  )
  .jvmSettings(
    scalaVersion := scala3,
    crossScalaVersions := Seq(scala212, scala211, scala213, scala3)
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
  .settings(
    name := "blake3-bench",
    skip in publish := true,
    assemblyJarName in assembly := "bench.jar",
    mainClass in assembly := Some("org.openjdk.jmh.Main"),
    test in assembly := {},
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
      case _ => MergeStrategy.first
    },
    assembly in Jmh := (assembly in Jmh).dependsOn(Keys.compile in Jmh).value
  )
  .enablePlugins(JmhPlugin)
