/*
package views.gui

import controllers.{PlayerController, RoundController}
import models.{GameModel, PlayerModel, RoundModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.swing.event.ButtonClicked
import scala.util.Success
import swing._

object SwingGUI extends SimpleSwingApplication {
  val boardController = new BoardController()
  val gameController = new GameController()
  val playerController = new PlayerController()
  val roundController = new RoundController()

  var game: Option[GameModel] = None
  var currentRound: Option[RoundModel] = None
  var player1: Option[PlayerModel] = None
  var player2: Option[PlayerModel] = None


  def top = new MainFrame() {
    title = "Connect4"
    preferredSize = new swing.Dimension(400, 400)

    // Welcome
    contents = new FlowPanel {
      val player1label = new Label {
        text = if(player1.isDefined) player1.get.name else ""
      }
      val player2Label = new Label {
        text = if(player1.isDefined) player1.get.name else ""
      }

      contents += player1label
      contents += player2Label


      val init = for {
        newGame <- gameController.create
        player1 <- playerController.create(newGame, "Fabi", "Red")
        player2 <- playerController.create(newGame, "Dimi", "Blue")
        gameWithPlayers <- {
          (player1, player2) match {
            case (Success(p1), Success(p2)) => gameController.addPlayers(newGame, Seq(p1._2, p2._2))
          }

        }
        firstRound <- roundController.create(gameWithPlayers.get)
      } yield firstRound

      init.map { firstRound =>
        println(firstRound)
        game = Some(firstRound._1)
        currentRound = Some(firstRound._2)
        player1 = Some(game.get.players(0))
        player1 = Some(game.get.players(1))
        repaint()
      }
    }

  }

}
*/
