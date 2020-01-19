package daos

import models.{GameModel, PlayerModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class GameDao {

  /**
   * Creates a game
   *
   * @return
   */
  def create: Future[GameModel] = Future {
    GameModel()
  }

  /**
   * Adds a player to the given game
   *
   * @param game        : game the player should be added to
   * @param playerToAdd : player to add to the game
   * @return
   */
  def addPlayer(game: GameModel, playerToAdd: PlayerModel): Future[GameModel] = Future {
    val players = playerToAdd :: game.players
    game.copy(players = players)
  }

  /**
   * Adds a list of players to the given game
   *
   * @param game         : game the players should be added to
   * @param playersToAdd : players to add to the game
   * @return
   */
  def addPlayers(game: GameModel, playersToAdd: List[PlayerModel]): Future[GameModel] = Future {
    val players = playersToAdd ::: game.players
    game.copy(players = players)
  }
}
