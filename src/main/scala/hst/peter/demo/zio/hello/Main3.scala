package hst.peter.demo.zio.hello

import zio.Schedule
import zio._
import zio.console._

object Main3 extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = myApp.exitCode

  val myApp = putStrLn("Hello, World").repeat(Schedule.recurs(3))
}
