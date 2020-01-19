package services

import models._
import org.scalatest.{Outcome, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class RoundServiceSpec extends WordSpec {
  val roundService = new RoundService()

  override def withFixture(test: NoArgTest): Outcome = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  "A round" can {
    "allows players to swap turn" in {
      val player1 = PlayerModel("Fabian")
      val player2 = PlayerModel("Dimitri")
      val players = List(player1, player2)
      val board = BoardModel()
      val round = RoundModel(board, players = players, currentPlayer = Some(player1))

      val result = roundService.swapTurns(round)
      val nextPlayer = result.currentPlayer
      assert(nextPlayer.get.name.equals(player2.name))
    }
    "can determine a winner" in {
      val player1 = PlayerModel("Fabian")
      val player2 = PlayerModel("Dimitri")
      val players = List(player1, player2)
      val chips = (0 to 4).map(index => ChipModel(player1, PositionModel(y = index))).toList
      val board = BoardModel(chips = chips)
      val round = RoundModel(board, players = players, currentPlayer = Some(player1))

      val result: Option[RoundModel] = Await.result(roundService.checkForWinner(round), Duration.Inf)
      assert(result.get.winner.isDefined)
    }
    "cannot determine a winner" in {
      val player1 = PlayerModel("Fabian")
      val player2 = PlayerModel("Dimitri")
      val players = List(player1, player2)
      val board = BoardModel()
      val round = RoundModel(board, players = players, currentPlayer = Some(player1))

      val result: Option[RoundModel] = Await.result(roundService.checkForWinner(round), Duration.Inf)
      assert(result.get.winner.isEmpty)
    }
  }
}
