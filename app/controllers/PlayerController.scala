package controllers

import daos.{BoardDao, GameDao, PlayerDao, RoundDao}
import javax.inject.{Inject, Singleton}
import models.{GameModel, PlayerModel}
import play.api.mvc.{BaseController, ControllerComponents}
import services.{GameService, PlayerService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}



@Singleton
class PlayerController @Inject()(val controllerComponents: ControllerComponents,
                               gameController: GameController,
                               boardController: BoardController,
                               playerService: PlayerService,
                               playerDao: PlayerDao,
                               roundDao: RoundDao,
                               boardDao: BoardDao
                              ) extends BaseController {

  /**
   * Creates a player
   *
   * @param name : name of the player
   * @return
   */
  def create(game: GameModel, name: String, color: String): Future[Try[(GameModel, PlayerModel)]] = {
    name match {
      case playerName if playerName.length > 1 => playerDao.create(name, color).map(x => Success((game, x)))
      case _ => Future.successful(Failure(new IllegalArgumentException(s"Name of player has to be at least one character long.")))
    }
  }

  /**
   * Updates a given player in the game
   *
   * @param game           : game for which the player needs to be updated
   * @param color : color of the player
   * @return
   */
  def updateColor(game: GameModel, player: PlayerModel, color: String): Future[Try[(GameModel, PlayerModel)]] = {
    color match {
      case playerColor if playerColor.length > 1 =>
        playerService.updateColor(game, player, color).map {
          case Success(updatedPlayer) => Success((game, updatedPlayer))
          case Failure(error) => Failure(error)
        }
      case _ => Future.successful(Failure(new IllegalArgumentException(s"Color has to be at least one character long.")))
    }
  }

}
