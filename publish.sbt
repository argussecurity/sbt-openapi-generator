organization := "com.argus-sec"
organizationName := "Argus Cyber Security Ltd."
organizationHomepage := Some(url("https://argus-sec.com"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/argussecurity/sbt-openapi-generator"),
    "scm:git@github.com:argussecurity/sbt-openapi-generator.git"
  )
)
developers := List(
  Developer(
    id = "adigerber",
    name = "Adi Gerber",
    email = "adi.gerber@argus-sec.com",
    url = url("https://github.com/adigerber")
  )
)
description := "An SBT plugin which invokes openapi-generator."
licenses := List("Apache-2.0" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/argussecurity/sbt-openapi-generator"))

pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true
