package hst.peter.demo.zio.tour

import java.io.IOException

import zio._
import zio.duration.durationInt

import scala.annotation.tailrec

object Hello extends App {

  import zio.console._

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = putStrLn("Hello,World!").exitCode
}

object PrintSec extends App {

  import zio.console._

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    //putStrLn("Hello").zipRight(putStrLn("world")).exitCode
    putStrLn("Hello") *> putStrLn("world").exitCode
  }
}

object ErrorApp extends App {

  import zio.console._

  val fail = putStrLn("About to fail...") *>
    ZIO.fail("Uh! ha Fail...") *>
    putStrLn("This is never be print")

  //  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = fail.exitCode
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = fail.catchAllCause(cause => putStrLn(cause.prettyPrint)).exitCode
}


object Looping extends App {

  import zio.console._

  def repeat[R, E, A](n: Int)(effect: ZIO[R, E, A]): ZIO[R, E, A] = if (n <= 1) effect else effect *> repeat(n - 1)(effect)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = repeat(1000)(putStrLn("Hello")).exitCode
}

object Prompt extends App {

  import zio.console._

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = putStrLn("What's your name?") *> getStrLn.flatMap(name => putStrLn(s"hello, $name!")).exitCode
}

object AlarmAppImproved extends App {

  import zio.console._
  import zio.duration._
  import java.util.concurrent.TimeUnit

  // 学会 try Left/Right 用法
  def toDouble(s: String): Either[NumberFormatException, Double] = try Right(s.toDouble) catch {
    case e: NumberFormatException => Left(e)
  }

  lazy val getAlarmDuration: ZIO[Console, IOException, Duration] = {
    def parseDuration(input: String): Either[NumberFormatException, Duration] = toDouble(input).map(double => Duration((double * 1000.0).toLong, TimeUnit.MILLISECONDS))

    // 持续循环执行, 直到成功
    val fallback = putStrLn("You didn't enter the number of seconds!") *> getAlarmDuration

    // for表达式简洁/强大
    for {
      _ <- putStrLn("Please enter the number of seconds to sleep: ")
      input <- getStrLn
      duration <- ZIO.fromEither(parseDuration(input)) orElse fallback
    } yield duration
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = getAlarmDuration.exitCode
}

object ComputePi extends App {

  import zio.random._
  import zio.console._

  final case class PiState(inside: Long, total: Long)

  def estimatePi(inside: Long, total: Long): Double = (inside.toDouble / total.toLong) * 4.0

  def insideCircle(x: Double, y: Double): Boolean = Math.sqrt(x * y + y * y) <= 1.0

  val randomPoint: ZIO[Random, Nothing, (Double, Double)] = nextDouble zip nextDouble

  def updateOnce(ref: Ref[PiState]): ZIO[Random, Nothing, Unit] = for {
    tuple <- randomPoint
    (x, y) = tuple
    inside = if (insideCircle(x, y)) 1 else 0
    _ <- ref.update(state => PiState(state.inside + inside, state.total + 1))
  } yield ()

  def printEstimate(ref: Ref[PiState]): ZIO[Console, Nothing, Unit] =
    for {
      state <- ref.get
      _ <- putStrLn(s"${estimatePi(state.inside, state.total)}")
    } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    ((for {
      ref <- Ref.make(PiState(0L, 0L))
      worker = updateOnce(ref).forever
      workers = List.fill(4)(worker)
      fiber1 <- ZIO.forkAll(workers)
      fiber2 <- (printEstimate(ref) *> ZIO.sleep(1.second)).forever.fork
      _ <- putStrLn("Enter any key to terminate...")
      //  _ <- getStrLn *> fiber1.interrupt *> fiber2.interrupt
      _ <- getStrLn *> (fiber1 zip fiber2).interrupt
    } yield 0) orElse ZIO.succeed(1)).exitCode
}
