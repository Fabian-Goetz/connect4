package services

import models.{BoardModel, ChipModel, PlayerModel, PositionModel}
import org.scalatest.{Outcome, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

class BoardServiceSpec extends WordSpec {
  val boardService = new BoardService()

  override def withFixture(test: NoArgTest): Outcome = { // Define a shared fixture
    // Shared setup (run at beginning of each test)
    try test()
    finally {
      // Shared cleanup (run at end of each test)
    }
  }

  "A board" can {
    "insert a chip" when {
      val position = PositionModel() // x = 0, y = 0
      val player = PlayerModel("Fabian")

      val chip = ChipModel(player, position = position)

      "the columns max height (height of board - 1) of the board is not reached yet" in {
        val existingChips = (0 until BoardModel().height - 1).map(index => ChipModel(player, position = PositionModel(0, index))).toList
        val newBoard = BoardModel(chips = existingChips)

        val result: Try[BoardModel] = Await.result(boardService.insertChip(newBoard, chip), Duration.Inf)
        assert(result.isInstanceOf[Success[BoardModel]])
      }
      "the column is empty" in {
        val newBoard = BoardModel()

        val result: Try[BoardModel] = Await.result(boardService.insertChip(newBoard, chip), Duration.Inf)
        assert(result.isInstanceOf[Success[BoardModel]])
      }
    }
    "not insert a chip" when {
      val position = PositionModel() // x = 0, y = 0
      val player = PlayerModel("Fabian")

      val chip = ChipModel(player, position = position)

      "the columns max height (height of board - 1) of the board is already reached" in {
        val existingChips = (0 until BoardModel().height).map(index => ChipModel(player, position = PositionModel(0, index))).toList
        val newBoard = BoardModel(chips = existingChips)

        val result: Try[BoardModel] = Await.result(boardService.insertChip(newBoard, chip), Duration.Inf)
        assert(result.isInstanceOf[Failure[BoardModel]])
      }
    }
    "detect 4 consecutive chips" when {
      val player = PlayerModel("Fabian")

      "4 chips are consecutive in the same row" in {
        val positions: Seq[PositionModel] = (0 until 4).map(index => PositionModel(x = index))
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position)).toList
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isDefined)
      }
      "4 chips are consecutive in the same column" in {
        val positions: Seq[PositionModel] = (0 until 4).map(index => PositionModel(y = index))
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position)).toList
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isDefined)
      }
      "4 chips are consecutive diagonal from left to right" in {
        val positions: Seq[PositionModel] = (0 until 4).map(index => PositionModel(x = index, y = index))
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position)).toList
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isDefined)
      }
      "4 chips are consecutive diagonal from right to left" in {
        val position1 = PositionModel(7) // x = 7, y = 0
        val position2 = PositionModel(6, 1)
        val position3 = PositionModel(5, 2)
        val position4 = PositionModel(4, 3)
        val positions = List(position1, position2, position3, position4)

        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isDefined)
      }
      "4 chips are at the same time consecutive in a row, column and diagonal" in {
        val positions1: List[PositionModel] = (0 until 4).map(index => PositionModel(x = index)).toList
        val positions2: List[PositionModel] = (0 until 4).map(index => PositionModel(y = index)).toList
        val positions3: List[PositionModel] = (0 until 4).map(index => PositionModel(x = index, y = index)).toList

        val positions: List[PositionModel] = positions1 ::: positions2 ::: positions3

        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isDefined)
      }
    }
    "not detect 4 consecutive chips" when {
      val player = PlayerModel("Fabian")
      "at least one chip is missing in row" in {
        val positions: Seq[PositionModel] = (0 until 3).map(index => PositionModel(x = index))
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position)).toList
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is skipped in row" in {
        val positions: List[PositionModel] = PositionModel(x = 4) :: (0 until 3).map(index => PositionModel(x = index)).toList
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is missing in column" in {
        val positions: Seq[PositionModel] = (0 until 3).map(index => PositionModel(y = index))
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position)).toList
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is skipped in column" in {
        val positions: Seq[PositionModel] = PositionModel(y = 4) :: (0 until 3).map(index => PositionModel(y = index)).toList
        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position)).toList
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is missing in diagonals from left to right" in {
        val position1 = PositionModel() // x = 0, y = 0
        val position2 = PositionModel(1, 1)
        val position3 = PositionModel(2, 2)
        val positions = List(position1, position2, position3)

        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is skipped in diagonals from left to right" in {
        val position1 = PositionModel() // x = 0, y = 0
        val position2 = PositionModel(1, 1)
        val position3 = PositionModel(2, 2)
        val position4 = PositionModel(4, 4)
        val positions = List(position1, position2, position3, position4)

        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is missing in diagonals from right to left" in {
        val position1 = PositionModel(x = 7) // x = 7, y = 0
        val position2 = PositionModel(6, 1)
        val position3 = PositionModel(5, 2)
        val positions = List(position1, position2, position3)

        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
      "at least one chip is skipped in diagonals from right to left" in {
        val position1 = PositionModel(x = 7) // x = 7, y = 0
        val position2 = PositionModel(6, 1)
        val position3 = PositionModel(5, 2)
        val position4 = PositionModel(3, 3)
        val positions = List(position1, position2, position3, position4)

        val chips: List[ChipModel] = positions.map(position => ChipModel(player, position))
        val board = BoardModel(chips = chips)

        val result: Option[List[ChipModel]] = Await.result(boardService.checkForWinningChips(board, player), Duration.Inf)
        assert(result.isEmpty)
      }
    }
    "determine the maximum consecutive number of integers in a list" when {
      "they are strictly consecutive" in {
        val list = List(0, 1, 2, 3, 4, 5)
        val isConsecutive = boardService.isConsecutive(list, 5)
        assert(isConsecutive)
      }
      "they are not strictly consecutive" in {
        val list = List(0, 1, 2, 3, 5, 6, 7)
        val isConsecutive = boardService.isConsecutive(list, 3)
        assert(isConsecutive)
      }
      "the consecutive streak gets interrupted" in {
        val list = List(0, 1, 2, 4, 6, 7)
        val isConsecutive = boardService.isConsecutive(list, 3)
        assert(!isConsecutive)
      }
    }
  }
}
