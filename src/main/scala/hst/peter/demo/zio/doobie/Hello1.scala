package hst.peter.demo.zio.doobie

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._

object Hello1 extends App {

  // 第1个程序
  val program1 = 42.pure[ConnectionIO]

  import doobie.util.ExecutionContexts

  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  // Transactor是一种数据类型，它知道如何连接到数据库，分发连接并清理它们；有了这些知识，它就可以进行转换ConnectionIO ~> IO，从而为我们提供了可以运行的程序。具体来说，它为我们提供了一个IO在运行时将连接到数据库并执行单个事务的功能。
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql://10.10.130.2:5432/app_pub", // connect URL (driver-specific)
    "pocsql", // user
    "WJppFcU#ry", // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous) // just for testing
  )
  val io = program1.transact(xa)
  println(io.unsafeRunSync())

  //第2个程序
  val program2 = sql"select 42".query[Int].unique
  val io2 = program2.transact(xa)
  println(io2.unsafeRunSync())

  val program3: ConnectionIO[(Int, Double)] =
    for {
      a <- sql"select 42".query[Int].unique
      b <- sql"select random()".query[Double].unique
    } yield (a, b)

  program3.transact(xa).unsafeRunSync()
  val program3a = {
    val a: ConnectionIO[Int] = sql"select 42".query[Int].unique
    val b: ConnectionIO[Double] = sql"select random()".query[Double].unique
    (a, b).tupled
  }
  println(program3a.transact(xa).unsafeRunSync())

  val valuesList = program3a.replicateA(5)
  val result = valuesList.transact(xa)
  result.unsafeRunSync().foreach(println)
}
