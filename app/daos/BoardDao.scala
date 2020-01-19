package daos

import models.BoardModel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BoardDao {
  /**
   * Creates a board
   *
   * @return
   */
  def create: Future[BoardModel] = Future {
    BoardModel()
  }
}
