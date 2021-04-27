package hst.peter.demo.zio.hello

import zio.console.{getStrLn, putStr, putStrLn}
import zio.{ExitCode, URIO}

object Main extends zio.App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = myAppLogic.exitCode

  val myAppLogic = for {
    _ <- putStrLn("Hello! What is your name?")
    name <- getStrLn
    _ <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
    _ <- putStr("Hello, World")
    _ <- getStrLn.flatMap(line => putStrLn(line))
  } yield ()
}
