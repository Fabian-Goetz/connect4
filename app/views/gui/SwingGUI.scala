package views.gui

import controllers.{BoardController, GameController}
import models.{BoardModel, CreateGameRequest, GameModel, RoundModel}
import utils.Observer
import views.{BoardControllerGui, GameControllerGui}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.swing.{Action, BorderPanel, BoxPanel, Button, Color, Dimension, FlowPanel, Font, Frame, GridPanel, Label, MainFrame, Menu, MenuBar, MenuItem, Orientation, Swing, TextField}
import scala.swing.event.{Event, Key, MouseClicked}
import scala.util.Try

class CellClicked(val row: Int, val column: Int) extends Event

class SwingGUI(gameController: GameControllerGui, boardController: BoardControllerGui) extends MainFrame with Observer {
  listenTo(gameController)
  listenTo(boardController)

  val player1 = CreateGameRequest(name = "Fabian", color = "red", hasTurn = false)
  val player2 = CreateGameRequest(name = "Dimitri", color = "blue", hasTurn = false)
  val players: Seq[CreateGameRequest] = Seq(player1, player2)

  var game: Try[GameModel] = Await.result(gameController.create(players), Duration.Inf)
  var round = game.get.rounds(0)
  var board = round.board

  title = "HTWG Connect4"


  def currentPlayer = new BoxPanel(Orientation.Horizontal) {
    contents += new Label("It's your turn, " + round.currentPlayer.get.name)
  }

  def winner = new BoxPanel(Orientation.Horizontal) {
    contents += new Label(round.winner.get.name + " has won!")
  }

  def insertChip(round: RoundModel, column: Int) = boardController.insertChip(round, column)

  def gridPanel = new GridPanel(game.get.rounds(0).board.width, game.get.rounds(0).board.height) {
    background = java.awt.Color.BLACK

    for {
      row <- game.get.rounds(0).board.height to 0 by -1
      column <- 0 until game.get.rounds(0).board.width
    } {
      if (row == game.get.rounds(0).board.height) {
        contents += new BoxPanel(Orientation.Horizontal) {
          val label: Label = new Label {
            text = (column + 1).toString
            font = new Font("Verdana", 1, 36)
          }

          contents += label
          preferredSize = new Dimension(51, 51)
          background = new Color(200, 200, 255)
          border = Swing.BeveledBorder(Swing.Raised)
          listenTo(mouse.clicks)
          listenTo(boardController)
          reactions += {
            case MouseClicked(src, pt, mod, clicks, pops) =>
              insertChip(round, column)
              repaint
          }
        }
      } else {
        contents += new BoxPanel(Orientation.Horizontal) {
          val chip = board.chips.find(x => x.position.x == column && x.position.y == row)

          val label: Label = new Label {
            text = if (chip.isDefined) chip.get.player.color else ""
            font = new Font("Verdana", 1, 36)
          }

          contents += label
          preferredSize = new Dimension(51, 51)
          background = new Color(200, 200, 255)
          border = Swing.BeveledBorder(Swing.Raised)
          listenTo(boardController)
        }
      }
    }
  }

  contents = new BorderPanel {
    add(currentPlayer, BorderPanel.Position.North)
    add(gridPanel, BorderPanel.Position.Center)
  }

  def gameOver = {
    contents = new BorderPanel {
      add(winner, BorderPanel.Position.Center)
    }
  }

  def redraw = {
    if (round.winner.isDefined) {
      gameOver
    } else {
      contents = new BorderPanel {
        add(currentPlayer, BorderPanel.Position.North)
        add(gridPanel, BorderPanel.Position.Center)
      }
    }
  }

  override def update(r: RoundModel): Unit = {
    round = r
    board = round.board
    redraw
  };
}