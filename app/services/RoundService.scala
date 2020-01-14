package services

import models.{BoardModel, ChipModel, GameModel, RoundModel}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class RoundService {
  val boardService = new BoardService()
  /**
   * Returns the next player
   *
   * @return
   */
  def swapTurns(round: RoundModel): RoundModel = {
    round.currentPlayer match {
      case Some(currentPlayer) =>
        val nextPlayer = round.players.filterNot(x => x.name == currentPlayer.name).headOption
        round.copy(currentPlayer = nextPlayer)
      case _ => round
    }
  }

  /**
   * Checks for a winner by looking for 4 consecutive chips in either a row or a column or diagonals.
   * If a winning series of chips is found, it returns a tuple consisting of:
   * (hasWon = true, player, winningChips).
   * If not found, it returns:
   * (hasWon = false, None, None)
   *
   * @return
   */
  def checkForWinner(round: RoundModel): Future[Option[List[ChipModel]]] = {
    round.currentPlayer match {
      case Some(currentPlayer) => boardService.checkForWinningChips(round.board, currentPlayer)
      case _ => Future.successful(None)
    }
  }

  /**
   * Returns the next round and updates:
   * - the board to empty
   * - players stay the same,
   * - current player swaps
   * - round number increments by 1
   *
   * If it can't find the next player, it throws an exception
   *
   * @return
   **/
  def nextRound(currentRound: RoundModel): RoundModel = {
    val swappedTurns: RoundModel = swapTurns(currentRound)
    val maybeNextPlayer = swappedTurns.players.find(_.hasTurn)
    maybeNextPlayer match {
      case Some(nextPlayer) =>
        RoundModel(
          board = BoardModel(),
          players = currentRound.players,
          currentPlayer = Some(nextPlayer),
          roundNumber = currentRound.roundNumber + 1
        )
      case _ => throw new InternalError(s"Something went wrong while instantiating a new round.")
    }
  }

  /**
   * Returns the current round based on the latest round number
   * @param game: game
   * @return
   */
  def getCurrentRound(game: GameModel): Future[Option[RoundModel]] = Future(game.rounds.sortBy(_.roundNumber).lastOption)
}
