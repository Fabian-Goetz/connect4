package services

import daos.GameDao
import models.{GameModel, PlayerModel, RoundModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}

class GameService {
  val gameDao = new GameDao()

  /**
   * Adds a player to a game if it's name isn't already taken.
   * Returns an error message if the name is already taken.
   *
   * @param game        : game to which to the new player needs to be added
   * @param playerToAdd : player to add
   * @return
   */
  def addPlayer(game: GameModel, playerToAdd: PlayerModel): Future[Try[GameModel]] = {
    val doesExist = game.players.exists(x => x.name == playerToAdd.name || x.color == playerToAdd.color)
    if (doesExist) {
      Future.successful(Failure(new IllegalArgumentException(s"The player $playerToAdd you want to add to the game $game does already exist.")))
    } else {
      gameDao.addPlayer(game, playerToAdd).map(x => Success(x))
    }
  }

  /**
   * Adds player to a game if it's name isn't already taken.
   * Returns an error message if the name is already taken.
   *
   * @param game         : game to which to the new player needs to be added
   * @param playersToAdd : players to add
   * @return
   */
  def addPlayers(game: GameModel, playersToAdd: Seq[PlayerModel]): Future[Try[GameModel]] = {
    val nameTooShort = playersToAdd.exists(p => p.name.length == 0)
    if(nameTooShort)
      return Future.successful(Failure(new IllegalArgumentException(s"Can't add players $playersToAdd to the game $game because of duplicates")))

    val doDuplicatesExist = playersToAdd.map(player => playersToAdd.count(x => x.name == player.name || x.color == player.color)).sum != playersToAdd.size

    if (doDuplicatesExist) {
      Future.successful(Failure(new IllegalArgumentException(s"Can't add players $playersToAdd to the game $game because of duplicates")))
    } else {
      val players: List[PlayerModel] = playersToAdd.toList ::: game.players
      gameDao.addPlayers(game, players).map(x => Success(x))
    }
  }

  /**
   * Returns the starting player of the game by random
   *
   * @param game : game
   * @return
   */
  def getRandomStartingPlayer(game: GameModel): Future[Option[PlayerModel]] = Future {
    game.players.length match {
      case length if length > 0 =>
        val n = util.Random.nextInt(game.players.size)
        val player: PlayerModel = game.players.iterator.drop(n).next
        Some(player)
      case _ => None
    }
  }

  /**
   * Return the current round of the game
   * @param game: game
   * @return
   */
  def getCurrentRound(game: GameModel): Future[Option[RoundModel]] = Future.successful(game.rounds.sortBy(_.roundNumber).lastOption)


}
