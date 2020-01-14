/*
package views.tui

import controllers.{BoardController, GameController, PlayerController, RoundController}
import models.{BoardModel, ChipModel, GameModel, PlayerModel, RoundModel}
import utils.Observer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class Index extends Observer {
  val gameController = new GameController()
  val playerController = new PlayerController()
  val roundController = new RoundController()
  val boardController = new BoardController()
  boardController.add(this)

  var game: Option[GameModel] = None
  var currentRound: Option[RoundModel] = None
  var player1: Option[PlayerModel] = None
  var player2: Option[PlayerModel] = None

  var input = 0



  def init() = {
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
    }
  }

  def welcome() = {
    println("Welcome to Connect4")
  }

  def play() = {
    println(currentRound.get.currentPlayer.get.name, " it's your turn!")
    println()
    printBoard(currentRound.get.board)
    val column = scala.io.StdIn.readLine().toInt - 1
    boardController.insertChip(currentRound.get, column)
  }

  def control() = {
    welcome()
    play()
  }

  override def update(board: BoardModel): Unit = {
    currentRound = Some(currentRound.get.copy(board = board))
    printBoard(currentRound.get.board)
    roundController.checkForWinner(currentRound.get).map {
      case Some(chips) => println("we have a winner!", chips)
      case _ => currentRound = Some(roundController.swapTurns(currentRound.get)); play()
    }
  }



 /* // Views
  val welcomeView = new Welcome
  val newPlayerView = new NewPlayer
  val newGameView = new NewGameView
  val roundView = new RoundView









  def welcome(): Unit = {
    println("Welcome to Connect 4")
  }

  def write(text: String): Unit = {
    println(text)
  }

  def read: String = {
    scala.io.StdIn.readLine()
  }


  def printBoard(game: GameModel) = {
    println("BOARD")
  }

  /**
   * Reads the column input
   *
   * @return
   */
  def readColumn(): Int = {
    val input: String = scala.io.StdIn.readLine()
    try {
      input.toInt
    } catch {
      case invalidFormat: NumberFormatException =>
        println((s"Wrong input format on $input. Please enter a number.\n" + invalidFormat))
        readColumn()
    }
  }


  def init() = {
    welcomeView.welcome()
    welcomeView.actions() match {
      case 'p' => newGame().map {
        case Some(newGame) =>
          newPlayer(newGame).flatMap(firstPlayer =>
            newPlayer(firstPlayer._1)
          ).flatMap(updatedGame =>
            newGameView.startGame(updatedGame._1)
          ).map(updatedGame =>
            roundView.currentRound(updatedGame)
          )
        case _ => endGame();
      }
      case _ => endGame()
    }
  }

  def newGame(): Future[Option[GameModel]] = newGameView.createNewGame()


  def newPlayer(game: GameModel): Future[(GameModel, PlayerModel)] = {
    newPlayerView.createPlayer(game).flatMap(newPlayer => {
      newPlayerView.addPlayer(newPlayer._1, newPlayer._2)
    })
  }

  def endGame() = Future {
    write("Game is over")
    System.exit(0)
  }
*/
  // Views: Welcome, Start game, add player, start round, board (with current player and insertChip)


  //
  //  def greetPlayers(players: List[Player]): Unit = {
  //
  //    print("Welcome, ")
  //    players.foreach(player => {
  //      print(player.name + ", ")
  //    })
  //    println("!")
  //  }
  //
   /**
    * Prints the board with all the chips on it
    * @param board: board to print
    */
   def printBoard(board: BoardModel): Unit = {
     // Print column numbers
     for (x <- 0 until board.width) {
       print(" " + (x + 1) + " ")
     }

     println()
     // Print board layout
     for (y <- board.height -1 to 0 by -1; x <- 0 until board.width) {
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

  //  def addPlayer(name: String, color: String) = {
  //
  //  }
}
*/
