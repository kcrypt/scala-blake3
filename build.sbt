import sbt.Keys.crossScalaVersions
import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scala210 = "2.10.7"
lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.13"
lazy val scala213 = "2.13.6"
lazy val scala3 = "3.0.0-RC3"

lazy val scalatestVersion = "3.2.8"

lazy val blake3jniVersion = "0.2.2"

name := "blake3"
organization in ThisBuild := "ky.korins"

dynverSeparator in ThisBuild := "-"

scalaVersion in ThisBuild := scala213
crossScalaVersions in ThisBuild := Seq()

scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.8",
  "-unchecked",
  "-deprecation"
)

publishTo in ThisBuild := sonatypePublishToBundle.value

headerLicense := LicenseDefinition.template

lazy val blake3 = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(AutomateHeaderPlugin)
  .in(file("."))
  .settings(
    skip in publish := false,
    publishArtifact in Test := false,
    buildInfoKeys := Seq(
      BuildInfoKey.action("commit") {
        scala.sys.process.Process("git rev-parse HEAD").!!.trim
      }
    ),
    headerLicense := LicenseDefinition.template,
    buildInfoPackage := "ky.korins.blake3",
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % scalatestVersion % Test,
    )
  )
  .jvmSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala210, scala211, scala212, scala213, scala3)
  )
  .jsSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala211, scala212, scala213, scala3),
  )
  .nativeSettings(
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala211, scala212, scala213),
    nativeLinkStubs := true,
  )

lazy val bench = project.in(file("bench"))
  .dependsOn(blake3.jvm)
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    name := "blake3-bench",
    skip in publish := true,
    assemblyJarName in assembly := "bench.jar",
    mainClass in assembly := Some("org.openjdk.jmh.Main"),
    test in assembly := {},
    libraryDependencies ++= Seq(
      "io.lktk" % "blake3jni" % blake3jniVersion,
    ),
    headerLicense := LicenseDefinition.template,
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", "MANIFEST.MF") =>
        MergeStrategy.discard
      case _ =>
        MergeStrategy.first
    },
    assembly in Jmh := (assembly in Jmh).dependsOn(Keys.compile in Jmh).value
  )
  .enablePlugins(JmhPlugin)
