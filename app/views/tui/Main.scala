package views.tui

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main {

  def main(args: Array[String]): Unit = {


   /* val index = new Index
    Await.result( index.init(), Duration.Inf)
    index.control()*/
    // Endless loop
    endlessLoop()
  }

  def endlessLoop(): Unit = while (true) Thread.sleep(10000)
}
