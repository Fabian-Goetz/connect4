package views.tui

import models._
import utils.Observer
import views.{BoardControllerGui, GameControllerGui}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

class Index(gameController: GameControllerGui, boardController: BoardControllerGui) extends Observer {

  boardController.add(this)

  var game: Try[GameModel] = _
  var round: RoundModel = _
  var board: BoardModel = _
  val player1 = CreateGameRequest(name = "Fabian", color = "red", hasTurn = false)
  val player2 = CreateGameRequest(name = "Dimitri", color = "blue", hasTurn = false)

  def startGame() = {
    init()
    welcome()
    play()
  }

  def init() = {
    val players: Seq[CreateGameRequest] = Seq(player1, player2)

    game = Await.result(gameController.create(players), Duration.Inf)
    round = game.get.rounds(0)
    board = round.board
  }

  def welcome() = {
    println("Welcome to Connect4")
  }

  def play() = {
    println(round.currentPlayer.get.name, " it's your turn!")
    println()
    printBoard(round.board)
    val column = scala.io.StdIn.readLine().toInt - 1
    boardController.insertChip(round, column)
  }

  /**
   *
   * Prints the board with all the chips on it
   *
   * @param board : board to print
   */
  def printBoard(board: BoardModel): Unit = {
    // Print column numbers
    for (x <- 0 until board.width) {
      print(" " + (x + 1) + " ")
    }

    println()
    // Print board layout
    for (y <- board.height - 1 to 0 by -1; x <- 0 until board.width) {
      val maybeChip: Option[ChipModel] = board.chips.find(chip => chip.position.x == x && chip.position.y == y)
      val charToPrint: Char = maybeChip match {
        case Some(chip) => chip.player.color(0)
        case _ => '_'
      }
      if (x == board.width - 1) {
        print(" " + charToPrint + " \n")
      } else {
        print(" " + charToPrint + " ")
      }
    }
  }

  def gameOver(): Unit = {
    println(round.winner.get.name, " has won!")
  }

  override def update(round: RoundModel): Unit = {
    this.round = round
    printBoard(round.board)
    if (round.winner.isDefined)
      gameOver()
    else play()
  }
}