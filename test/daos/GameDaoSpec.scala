package daos

import daos.GameDao
import models.{GameModel, PlayerModel}
import org.scalatest.{Outcome, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GameDaoSpec extends WordSpec {
  val gameDao = new GameDao()

  override def withFixture(test: NoArgTest): Outcome = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  "A game" can {
    "be created" in {
      val result = Await.result(gameDao.create, Duration.Inf)
      assert(result.isInstanceOf[GameModel])
    }
    "add a player to the game" in {
      val game = GameModel()
      val player = PlayerModel("Max Mustermann")
      val result = Await.result(gameDao.addPlayer(game, player), Duration.Inf)
      assert(result.isInstanceOf[GameModel])
    }
    "adds a list of players to the game" in {
      val game = GameModel()
      val player1 = PlayerModel("Max Mustermann 1")
      val player2 = PlayerModel("Max Mustermann 2")
      val player3 = PlayerModel("Max Mustermann 3")
      val players = List(player1, player2, player3)
      val result = Await.result(gameDao.addPlayers(game, players), Duration.Inf)
      assert(result.isInstanceOf[GameModel])
    }
  }
}
