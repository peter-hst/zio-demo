package hst.peter.demo.zio.effects

import zio._
import zio.console._

/**
 * 创建Effects
 */
object CreateingEffeccts extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = succeed.exitCode

  val succeed =  for {
    v <- ZIO.succeed(41)
      _ <- putStrLn(v.toString)
  } yield ()
}
