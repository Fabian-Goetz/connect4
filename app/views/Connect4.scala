package views

import java.awt.GraphicsEnvironment
import java.io.BufferedReader

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.{Guice, Injector}
import controllers.{BoardController, GameController}
import javax.inject.{Inject, Singleton}
import views.gui.{BoardControllerGui, GameControllerGui, SwingGUI}

import scala.concurrent.Await
import scala.concurrent.duration._

object Connect4 {

  def main(args: Array[String]): Unit = {
    val gameController = new GameControllerGui
    val boardController = new BoardControllerGui
    val gui = new SwingGUI(gameController, boardController)

    boardController.add(gui)

    gui.visible = true
    gui.repaint()
  }
}
