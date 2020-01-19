package daos

import models.{BoardModel, RoundModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RoundDao {

  /**
   * Creates a new round
   *
   * @param board : board for the round
   * @return
   */
  def create(board: BoardModel): Future[RoundModel] = Future {
    RoundModel(board = board)
  }


  def update(round: RoundModel): Future[RoundModel] = Future {
    round
  }
}
