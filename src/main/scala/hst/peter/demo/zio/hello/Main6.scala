package hst.peter.demo.zio.hello

import zio._
import zio.console._

import scala.concurrent.Future

object Main6 extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = logic.exitCode

  def list = 1 to 1000 to List

  def list2 = ZIO.succeed {
    Thread.sleep(500)
    list
  }

/*
  def futures = ZIO.fromFuture(Future {
    putStrLn("Hello")
  })
*/

  def test = list2

  def logic = for {
    l <- list2
    _ <- ZIO.foreachPar_(l)(i => putStrLn(i.toString))
  } yield ()
}
