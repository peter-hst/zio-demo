package hst.peter.demo.zio.hello

import zio.console.putStrLn
import zio.{App, ExitCode, Schedule, URIO}

object Main4 extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = putStrLn("Hello,World").repeat(Schedule.recurs(2)).exitCode
}
