import SiteKeys._
import GhPagesKeys._
import GitKeys._

organization := "com.livestream"

name := "scredis"

version := "1.1.2"

scalaVersion := "2.11.1"

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.5" intransitive (),
  "org.apache.commons" % "commons-lang3" % "3.1",
  "commons-pool" % "commons-pool" % "1.6",
  "com.typesafe" % "config" % "1.2.0",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test"
)

publishTo <<= version { (v: String) =>
  val repository = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at repository + "content/repositories/snapshots")
  else
    Some("releases" at repository + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/Livestream/scredis</url>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:Livestream/scredis.git</url>
    <connection>scm:git:git@github.com:Livestream/scredis.git</connection>
  </scm>
  <developers>
    <developer>
      <id>curreli</id>
      <name>Alexandre Curreli</name>
      <url>https://github.com/curreli</url>
    </developer>
  </developers>
)

parallelExecution in Test := false

concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

site.settings

ghpages.settings

siteMappings <++= (mappings in packageDoc in Compile, version) map { (m, v) =>
  for ((f, d) <- m) yield (
    f,
    if (v.trim.endsWith("SNAPSHOT")) ("api/snapshot/" + d) else ("api/"+v+"/"+d)
  )
}

synchLocal <<= (privateMappings, updatedRepository, gitRunner, streams) map { (mappings, repo, git, s) =>
  val betterMappings = mappings map { case (file, target) => (file, repo / target) }
  IO.copy(betterMappings)
  repo
}

git.remoteRepo := "git@github.com:Livestream/scredis.git"
