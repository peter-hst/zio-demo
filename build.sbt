name := "zio-demo"

version := "0.1"

scalaVersion := "2.13.5"

lazy val doobieVersion = "0.12.1"

libraryDependencies ++= Seq(

  // zio
  "dev.zio" %% "zio" % "1.0.5",
  "dev.zio" %% "zio-streams" % "1.0.5",
  // Start with this one
  "org.tpolecat" %% "doobie-core" % doobieVersion,

  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2" % doobieVersion, // H2 driver 1.4.200 + type mappings.
  "org.tpolecat" %% "doobie-hikari" % doobieVersion, // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres" % doobieVersion, // Postgres driver 42.2.19 + type mappings.
  "org.tpolecat" %% "doobie-quill" % doobieVersion, // Support for Quill 3.6.1
  "org.tpolecat" %% "doobie-specs2" % doobieVersion % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % "test" // ScalaTest support for typechecking statements.
)
