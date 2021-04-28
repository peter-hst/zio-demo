package hst.peter.demo.zio.doobie

import cats.effect.{Blocker, IO}
import cats.implicits._
import doobie.{KleisliInterpreter, _}
import doobie.implicits._
import doobie.util.ExecutionContexts

object HelloDeeper2 extends App {
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  val program1 = 42.pure[ConnectionIO]
  val interpreter = KleisliInterpreter[IO](Blocker.liftExecutionContext(ExecutionContexts.synchronous)).ConnectionInterpreter
  val kleisli = program1.foldMap(interpreter)
  val io3 = IO(null: java.sql.Connection) >>= kleisli.run
  println(io3.unsafeRunSync())
}
