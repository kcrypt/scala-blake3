addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.0.0")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.0.0")

val scalaJSVersion =
  Option(System.getenv("SCALAJS_VERSION")).getOrElse("1.5.1")
val scalaNativeJSVersion =
  Option(System.getenv("SCALANATIVE_VERSION")).getOrElse("0.4.0")

addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % scalaJSVersion)
addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % scalaNativeJSVersion)
addSbtPlugin("ch.epfl.lamp"       % "sbt-dotty"                     % "0.5.3")

addSbtPlugin("pl.project13.scala" % "sbt-jmh"                       % "0.4.0")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"                       % "2.1.1")
addSbtPlugin("org.xerial.sbt"     % "sbt-sonatype"                  % "3.9.5")
addSbtPlugin("com.eed3si9n"       % "sbt-buildinfo"                 % "0.10.0")
addSbtPlugin("com.eed3si9n"       % "sbt-assembly"                  % "0.15.0")
addSbtPlugin("com.dwijnand"       % "sbt-dynver"                    % "4.1.1")
addSbtPlugin("org.scalameta"      % "sbt-scalafmt"                  % "2.4.2")
addSbtPlugin("de.heikoseeberger"  % "sbt-header"                    % "5.6.0")