package controllers

import daos.BoardDao
import javax.inject._
import models.{BoardModel, ChipModel, PositionModel, RoundModel}
import play.api.mvc._
import services.BoardService
import utils.Observable

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BoardController @Inject()(controllerComponents: ControllerComponents,
                                boardService: BoardService,
                                boardDao: BoardDao
                                ) extends AbstractController(controllerComponents)  with Observable {

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
  def insertChip(round: RoundModel, column: Int): Future[Try[BoardModel]] = {
    round.currentPlayer match {
      case Some(player) =>
        val chip = ChipModel(player, PositionModel(x = column))
        boardService.insertChip(round.board, chip).map {
          case Success(b) => notifyObservers(b)
            Success(b)
          case e => e
        }
      case _ => Future.successful(Failure(new NoSuchElementException(s"Current player doesn't exist")))
    }
  }
}
