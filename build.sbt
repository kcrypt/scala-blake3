import sbtcrossproject.CrossPlugin.autoImport.crossProject

lazy val scala211 = "2.11.12"
lazy val scala212 = "2.12.12"
lazy val scala213 = "2.13.3"

lazy val dotty = "0.27.0-RC1"

lazy val scalatestVersion = "3.2.2"

name := "blake3"
organization in ThisBuild := "ky.korins"
version in ThisBuild := "1.5.0"
scalaVersion in ThisBuild := scala213
crossScalaVersions in ThisBuild := Seq(scala212, scala211, scala213, dotty)
scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

publishTo in ThisBuild := sonatypePublishToBundle.value
sonatypeProfileName in ThisBuild := "ky.korins"
publishMavenStyle in ThisBuild := true
sonatypeProjectHosting in ThisBuild := Some(xerial.sbt.Sonatype.GitHubHosting("catap", "scala-blake3", "kirill@korins.ky"))
licenses in ThisBuild := Seq("The Unlicense" -> url("https://github.com/catap/scala-blake3/blob/master/LICENSE.txt"))
homepage in ThisBuild := Some(url("https://github.com/catap/scala-blake3"))
scmInfo in ThisBuild := Some(
  ScmInfo(
    url("https://github.com/catap/scala-blake3"),
    "scm:git@github.com:catap/scala-blake3.git"
  )
)
developers in ThisBuild := List(
  Developer(id="catap", name="Kirill A. Korinsky", email="kirill@korins.ky", url=url("https://github.com/catap"))
)

skip in publish := true

lazy val blake3 = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .settings(
    skip in publish := false,
    publishArtifact in Test := false,
    libraryDependencies ++= Seq(
      "org.scalatest" %%% "scalatest" % scalatestVersion % Test,
    )
  )
  .jsSettings(
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
  )
  .enablePlugins(JmhPlugin)
