import sbt.url
import xerial.sbt.Sonatype.GitHubHosting

sonatypeProfileName in ThisBuild := "ky.korins"
publishMavenStyle in ThisBuild := true
sonatypeProjectHosting in ThisBuild := Some(GitHubHosting("catap", "scala-blake3", "kirill@korins.ky"))
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