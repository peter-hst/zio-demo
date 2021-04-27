package hst.peter.demo.zio.hello

import zio.console.putStrLn
import zio.{App, ExitCode, Schedule, URIO}

object Main5 extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = sayHelloTwice.exitCode

  def sayHello = putStrLn("Hello,World")

  def sayHelloTwice = sayHello.repeat(Schedule.recurs(3))

  val myAppLogic = putStrLn("Hello, Shanghai!")
}
