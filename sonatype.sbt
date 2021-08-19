import sbt.url
import xerial.sbt.Sonatype.GitHubHosting

ThisBuild / sonatypeProfileName := "ky.korins"
ThisBuild / publishMavenStyle := true
ThisBuild / sonatypeProjectHosting := Some(
  GitHubHosting("catap", "scala-blake3", "kirill@korins.ky")
)
ThisBuild / licenses := LicenseDefinition.licenses
ThisBuild / homepage := Some(url("https://github.com/catap/scala-blake3"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/catap/scala-blake3"),
    "scm:git@github.com:catap/scala-blake3.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "catap",
    name = "Kirill A. Korinsky",
    email = "kirill@korins.ky",
    url = url("https://github.com/catap")
  )
)
