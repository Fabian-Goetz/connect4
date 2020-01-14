package daos

import models.PlayerModel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PlayerDao {
  /**
   * Creates a player
   *
   * @param name : name of the player
   * @return
   */
  def create(name: String, color: String): Future[PlayerModel] = Future {
    PlayerModel(name = name, color = color)
  }


  /**
   * Updates a given player in the game
   * @param playerToUpdate: player to update
   * @return
   */
  def update(playerToUpdate: PlayerModel): Future[PlayerModel] = Future {
    playerToUpdate
  }

}
