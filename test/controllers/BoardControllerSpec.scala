package controllers

import models.{BoardModel, ChipModel, GameModel, PlayerModel, PositionModel, RoundModel}
import org.scalatest.{Outcome, WordSpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.mvc.AnyContentAsJson

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class BoardControllerSpec extends WordSpec {

  val application: Application = GuiceApplicationBuilder().build()
  val boardController: BoardController = getBoardController(application)

  def getBoardController(implicit app: Application): BoardController = {
    val app2ProcessInstancesController = Application.instanceCache[controllers.BoardController]
    app2ProcessInstancesController(app)
  }

  override def withFixture(test: NoArgTest): Outcome = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  "A board" can {
    "can insert a chip into an empty board" in {
      val player = PlayerModel("Fabian")
      val board = BoardModel()
      val round = RoundModel(board, currentPlayer = Some(player))

      val result = Await.result(boardController.insertChip(round, 0), Duration.Inf)
      assert(result.isInstanceOf[Success[Some[RoundModel]]])
    }
    "can insert a chip into a board" in {
      val player = PlayerModel("Fabian")
      val chips = (0 to 3).map(index => ChipModel(player, PositionModel(y = index))).toList
      val board = BoardModel(chips = chips)
      val round = RoundModel(board, currentPlayer = Some(player))

      val result = Await.result(boardController.insertChip(round, 0), Duration.Inf)
      assert(result.isInstanceOf[Success[Some[RoundModel]]])
    }
    "cannot insert a chip into a full column" in {
      val player = PlayerModel("Fabian")
      val chips = (0 to 6).map(index => ChipModel(player, PositionModel(y = index))).toList
      val board = BoardModel(chips = chips)
      val round = RoundModel(board, currentPlayer = Some(player))

      val result = Await.result(boardController.insertChip(round, 0), Duration.Inf)
      assert(result.isInstanceOf[Failure[Some[RoundModel]]])
    }
    "cannot insert a chip into a non existing column" in {
      val player = PlayerModel("Fabian")
      val board = BoardModel()
      val round = RoundModel(board, currentPlayer = Some(player))

      val result = Await.result(boardController.insertChip(round, 9), Duration.Inf)
      assert(result.isInstanceOf[Failure[Some[RoundModel]]])
    }
    "cannot insert a chip without a current player" in {
      val board = BoardModel()
      val round = RoundModel(board)

      val result = Await.result(boardController.insertChip(round, 0), Duration.Inf)
      assert(result.isInstanceOf[Failure[Some[RoundModel]]])
    }
  }
}
