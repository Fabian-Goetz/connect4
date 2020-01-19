package daos

import models.{BoardModel, ChipModel, PlayerModel, PositionModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ChipDao {
  /**
   * Creates a board
   *
   * @return
   */
  def create(player: PlayerModel, position: PositionModel): Future[ChipModel] = Future {
    ChipModel(player, position)
  }
}
