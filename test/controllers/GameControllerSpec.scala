package controllers

import models.{GameModel, PlayerModel}
import org.scalatest.{Outcome, WordSpec}
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

class GameControllerSpec extends WordSpec {

  val app = new GuiceApplicationBuilder().build()

  val gameController = getGameController(app)


  def getGameController(implicit app: Application): GameController = {
    val app2ProcessInstancesController = Application.instanceCache[controllers.GameController]
    app2ProcessInstancesController(app)
  }


  override def withFixture(test: NoArgTest): Outcome = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  "A game" can {
    "be created" in {
      val result = Await.result(gameController.create, Duration.Inf)
      assert(result.isInstanceOf[GameModel])
    }
    "add a player to the game" in {
      val game = GameModel()
      val player = PlayerModel("Max Mustermann")
      val result = Await.result(gameController.addPlayer(game, player), Duration.Inf)
      assert(result.isInstanceOf[Success[GameModel]])
    }
    "refuses to add a player to the game if it already exists" in {
      val player1 = PlayerModel("Max Mustermann 1")
      val game = GameModel(players = List(player1))
      val player2 = PlayerModel("Max Mustermann 1")
      val result = Await.result(gameController.addPlayer(game, player2), Duration.Inf)
      assert(result.isInstanceOf[Failure[GameModel]])
    }
    "adds a list of players to the game" in {
      val game = GameModel()
      val player1 = PlayerModel("Max Mustermann 1")
      val player2 = PlayerModel("Max Mustermann 2", color = "red")
      val player3 = PlayerModel("Max Mustermann 3", color = "blue")
      val players = List(player1, player2, player3)
      val result = Await.result(gameController.addPlayers(game, players), Duration.Inf)
      assert(result.isInstanceOf[Success[GameModel]])
    }
    "refuses to add players to the game if duplicates exist" in {
      val game = GameModel()
      val player1 = PlayerModel("Max Mustermann 1")
      val player2 = PlayerModel("Max Mustermann 1")
      val player3 = PlayerModel("Max Mustermann 3")
      val players = List(player1, player2, player3)
      val result = Await.result(gameController.addPlayers(game, players), Duration.Inf)
      assert(result.isInstanceOf[Failure[GameModel]])
    }
    "returns the starting player for a game by random" in {
      val player1 = PlayerModel("Max Mustermann 1")
      val player2 = PlayerModel("Max Mustermann 2")
      val player3 = PlayerModel("Max Mustermann 3")
      val players = List(player1, player2, player3)
      val game = GameModel(players = players)
      val result = Await.result(gameController.getStartingPlayer(game), Duration.Inf)
      assert(result.isDefined)
    }
    "returns none for the starting player for a game if no player exists" in {
      val game = GameModel()
      val result = Await.result(gameController.getStartingPlayer(game), Duration.Inf)
      assert(result.isEmpty)
    }
  }
}
