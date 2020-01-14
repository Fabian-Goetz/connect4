package services

import daos.PlayerDao
import models.{GameModel, PlayerModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class PlayerService {
  val playerDao = new PlayerDao()

  /**
   * Updates a player if it doesn't already exist
   *
   * @param game  : game
   * @param color : color of the player
   * @return
   */
  def updateColor(game: GameModel, player: PlayerModel, color: String): Future[Try[PlayerModel]] = Future {
    // Is color taken by another player?
    val isTaken = game.players.find(x => x.color == color && x.name != player.name)

    isTaken match {
      case Some(player) => Failure(new IllegalArgumentException(s"The color $color is already taken by $player"))
      case _ => Success(player.copy(color = color))
    }
  }
}
