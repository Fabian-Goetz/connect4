package controllers

import daos.{BoardDao, PlayerDao, RoundDao}
import javax.inject.{Inject, Singleton}
import models.{BoardModel, ChipModel, GameModel, RoundModel}
import play.api.mvc.{BaseController, ControllerComponents}
import services.{BoardService, GameService, PlayerService, RoundService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoundController @Inject()(val controllerComponents: ControllerComponents,
                                 gameController: GameController,
                                 boardController: BoardController,
                                gameService: GameService,
                                 playerService: PlayerService,
                                boardService: BoardService,
                                roundService: RoundService,
                                 playerDao: PlayerDao,
                                 roundDao: RoundDao,
                                 boardDao: BoardDao
                                ) extends BaseController {

  /**
   * Creates a new round. Returns the entire game and the new round as tuple
   *
   * @param game : game
   * @return
   */
  def create(game: GameModel): Future[(GameModel, RoundModel)] = {
    val actions = for {
      newBoard: BoardModel <- boardController.create
      startingPlayer <- gameController.getStartingPlayer(game)
      newRound: RoundModel <- roundDao.create(board = newBoard)
      latestRound <- gameService.getCurrentRound(game)
      updatedRound: RoundModel <- roundDao.update(newRound.copy(players = game.players, currentPlayer = startingPlayer, roundNumber = if (latestRound.isDefined) latestRound.get.roundNumber + 1 else 1))
    } yield updatedRound

    actions.map { updatedRound =>
      val rounds = updatedRound :: game.rounds
      (game.copy(rounds = rounds), updatedRound)
    }
  }

  /**
   * Returns the current round of the given game
   * @param game: game
   * @return
   */
  def getCurrentRound(game: GameModel): Future[Option[RoundModel]] = roundService.getCurrentRound(game)

  /**
   * Checks for possible winner
   * @param round: round
   * @return
   */
  def checkForWinner(round: RoundModel): Future[Option[List[ChipModel]]] = roundService.checkForWinner(round)

  /**
   * Swaps the current player of the given round
   * @param round: round
   * @return
   */
  def swapTurns(round: RoundModel): RoundModel = roundService.swapTurns(round)
}
