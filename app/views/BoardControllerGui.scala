package views

import daos.BoardDao
import javax.inject._
import models.{BoardModel, ChipModel, PositionModel, RoundModel}
import services.{BoardService, RoundService}
import utils.Observable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.swing.Publisher
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BoardControllerGui extends Publisher with Observable {
  val boardDao = new BoardDao
  val boardService = new BoardService
  val roundService = new RoundService


  /**
   * Creates a board
   *
   * @return
   */
  def create: Future[BoardModel] = boardDao.create

  /**
   * Let's the player insert a chip into the board
   *
   * @param round  : round
   * @param column : column to insert the chip
   * @return
   */
  def insertChip(round: RoundModel, column: Int): Future[Any] = {
    round.currentPlayer match {
      case Some(player) =>
        val chip = ChipModel(player, PositionModel(x = column))
        boardService.insertChip(round.board, chip).flatMap {
          case Success(b) => // TODO check for winner
            roundService.checkForWinner(round.copy(board = b)).map {
              case Some(winnerRound) => notifyObservers(roundService.swapTurns(winnerRound))
              case _ => notifyObservers(roundService.swapTurns(round))
            }
          case Failure(exception) => Future.successful(Failure(exception))
        }
      case _ => Future.successful(Failure(new NoSuchElementException(s"Current player doesn't exist")))
    }
  }
}