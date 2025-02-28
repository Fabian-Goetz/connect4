package controllers

import daos.BoardDao
import javax.inject._
import models.{ChipModel, PositionModel, RoundModel}
import play.api.mvc._
import services.{BoardService, RoundService}
import utils.Observable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.swing.Publisher
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BoardController @Inject()(controllerComponents: ControllerComponents,
                                boardService: BoardService,
                                roundService: RoundService,
                                boardDao: BoardDao
                               ) extends AbstractController(controllerComponents) with Observable with Publisher {

  /**
   * Let's the player insert a chip into the board
   *
   * @param round  : round
   * @param column : column to insert the chip
   * @return
   */
  def insertChip(round: RoundModel, column: Int): Future[Try[Option[RoundModel]]] = {
    round.currentPlayer match {
      case Some(player) =>
        val chip = ChipModel(player, PositionModel(x = column))
        boardService.insertChip(round.board, chip).flatMap {
          case Success(b) =>
            roundService.checkForWinner(round.copy(board = b)).map {
              case Some(winnerRound) => Success(Some(roundService.swapTurns(winnerRound)))
              case _ => Success(Some(roundService.swapTurns(round)))
            }
          case Failure(exception) => Future.successful(Failure(exception))
        }
      case _ => Future.successful(Failure(new NoSuchElementException(s"Current player doesn't exist")))
    }
  }
}
