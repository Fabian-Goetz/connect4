package controllers

import daos.{BoardDao, GameDao, RoundDao}
import javax.inject._
import models.{BoardModel, ChipModel, CreateGameRequest, GameModel, PlayerModel}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AnyContent, ControllerComponents, _}
import services.GameService

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class GameController @Inject()(val controllerComponents: ControllerComponents,
                               roundController: RoundController,
                               boardController: BoardController,
                               gameService: GameService,
                               gameDao: GameDao,
                               roundDao: RoundDao,
                               boardDao: BoardDao
                               ) extends BaseController {

  /**
   * Creates a new game with a new round and 2 players
   * @param request: request for the 2 players
   * @return
   */
  def create(request: Seq[CreateGameRequest]): Future[Try[GameModel]] = {
    val actions = for {
      newGame <- gameDao.create
      newBoard: BoardModel <- boardDao.create
      firstRound <- roundDao.create(newBoard)
      players <- gameService.addPlayers(newGame, request.map(x => PlayerModel(name = x.name, color = x.color)))
      startingPlayer <- getStartingPlayer(newGame)
    } yield (newGame, newBoard, firstRound, players, startingPlayer)

    actions.map { case (newGame, newBoard, firstRound, t_players, startingPlayer) =>
      t_players match {
        case Success(game) =>
          val round = firstRound.copy(board = newBoard, players = game.players, currentPlayer = startingPlayer)
          val updatedGame = newGame.copy(rounds = List(round), players = game.players, startsWith = startingPlayer)
          Success(updatedGame)
        case Failure(error) => Failure(error)
      }
    }
  }


  /**
   * Creates a game
   *
   * @return
   */
  def create: Future[GameModel] = gameDao.create

  /**
   * Adds player to the given game
   *
   * @param game        : game
   * @param playerToAdd : player to add
   * @return
   */
  def addPlayer(game: GameModel, playerToAdd: PlayerModel): Future[Try[GameModel]] = gameService.addPlayer(game, playerToAdd)

  /**
   * Adds players to the given game
   *
   * @param game         : game
   * @param playersToAdd : players to add
   * @return
   */
  def addPlayers(game: GameModel, playersToAdd: Seq[PlayerModel]): Future[Try[GameModel]] = gameService.addPlayers(game, playersToAdd)

  /**
   * Returns the starting player of the given game
   *
   * @param game : game
   * @return
   */
  def getStartingPlayer(game: GameModel): Future[Option[PlayerModel]] = gameService.getRandomStartingPlayer(game)

}
