package hst.peter.demo.zio.hello

import zio.{Runtime, Task}

/**
 * 标准scala程序或已有依赖注入的项目中使用zio的方式
 * 1.关于runtime,理想情况下,应当整个项目只有一个runtime,因为每个runtime都有自己的资源和线程池
 */
object Main2 extends scala.App {
  val runtime = Runtime.default
  runtime.unsafeRun(Task(println("Hello, World!")))
}
