package hst.peter.demo.zio.effects

import java.io.IOException

import zio._
import zio.console._

/**
 * 组合Effects
 */
object CombiningEffects extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = greet2().exitCode

  def getName(): ZIO[Console,IOException,String] = ZIO.succeed("Peter")

  def sayHello(name:String) :ZIO[Console, Nothing,Unit] = putStrLn(s"Hello, ${name}")

  def greet(): ZIO[Console, IOException, Unit] = for{
    name <- getName()
    _ <- sayHello(name)
  } yield ()

  def greet2() = getName.flatMap(sayHello(_))
}
