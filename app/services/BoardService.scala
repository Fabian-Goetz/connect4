package services

import models.{BoardModel, ChipModel, PlayerModel}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class BoardService {
  /**
   * Lets the player insert a chip into the board. If the max height (6) of a specific column is reached, the
   * function will return an an failure. If succeeded, it returns a success with the updated board
   *
   * @param chip : chip to insert into the board
   * @return
   **/
  def insertChip(board: BoardModel, chip: ChipModel): Future[Try[BoardModel]] = Future {
    if (chip.position.x > board.width || chip.position.x < 0) {
      Failure(new IllegalArgumentException(s"Column ${chip.position.x + 1} doesn't exist. Cannot insert $chip"))
    } else {
      val sameColumn: List[ChipModel] = board.chips.filter(_.position.x == chip.position.x)
      val maybeMaxHeight: Option[Int] = sameColumn.map(_.position.y).maxOption
      maybeMaxHeight match {
        case Some(maxHeight) =>
          if (maxHeight < board.height - 1) {
            val newPosition = chip.position.copy(y = maxHeight + 1)
            val newChip = chip.copy(position = newPosition)
            val newBoard = board.copy(chips = newChip :: board.chips)
            Success(newBoard)
          } else {
            Failure(new IllegalArgumentException(s"Column ${chip.position.x + 1} is already full. Cannot insert $chip"))
          }
        case _ => // Column is empty
          val newPosition = chip.position.copy(y = 0)
          val newChip = chip.copy(position = newPosition)
          val newBoard = board.copy(chips = newChip :: board.chips)
          Success(newBoard)
      }
    }
  }

  /**
   * Checks if the board has 4 consecutive chips of a given player.
   * - Checks for 4s in all rows,
   * - columns and
   * - diagonals
   *
   * @param board  : the board on which to check for
   * @param player : the player for which to check for
   * @return
   */
  def checkForWinningChips(board: BoardModel, player: PlayerModel): Future[Option[List[ChipModel]]] = Future {
    val chipsOfPlayer: Seq[ChipModel] = board.chips.filter(_.player.name == player.name)

    def inRow: Seq[ChipModel] = for {
      y <- 0 until board.height
      tempChips <- chipsOfPlayer.filter(_.position.y == y)
      if isConsecutive(chipsOfPlayer.filter(_.position.y == y).sortBy(_.position.x).map(_.position.x), 3)
    } yield tempChips

    def inColumn: Seq[ChipModel] = for {
      x <- 0 until board.width
      tempChips <- chipsOfPlayer.filter(_.position.x == x)
      if isConsecutive(chipsOfPlayer.filter(_.position.x == x).sortBy(_.position.y).map(_.position.y), 3)
    } yield tempChips

    // Check diagonals from right to left
    val fromXBottom = for {
      x <- 0 until board.width
    } yield for {
      y <- 0 until board.height
      tempChips <- chipsOfPlayer.filter(chip => chip.position.x == x + y && chip.position.y == y)
    } yield tempChips

    val fromYBottom = for {
      y <- 1 until board.height
    } yield for {
      x <- 0 until board.width
      tempChips <- chipsOfPlayer.filter(chip => chip.position.x == x && chip.position.y == y + x)
    } yield tempChips

    // Check diagonals from left to right
    val fromXRoof = for {
      x <- board.width to 0 by -1
    } yield for {
      y <- 0 until board.height
      tempChips <- chipsOfPlayer.filter(chip => chip.position.x == x - y && chip.position.y == y)
    } yield tempChips

    val fromYRoof = for {
      y <- 1 until board.height
    } yield for {
      x <- board.width to 0 by -1
      tempChips <- chipsOfPlayer.filter(chip => chip.position.x == x && chip.position.y == y + (board.width - x))
    } yield tempChips

    val leftToRight = fromXBottom ++ fromYBottom
    val rightToLeft = fromXRoof ++ fromYRoof

    val filteredLeftToRight: List[ChipModel] = leftToRight.filter(a => isConsecutive(a.sortBy(_.position.x).map(_.position.x), 3)).flatten.toList
    val filteredRightToLeft: List[ChipModel] = rightToLeft.filter(a => isConsecutive(a.sortBy(_.position.x).map(_.position.x), 3)).flatten.toList
    val diagonals: List[ChipModel] = filteredLeftToRight ++ filteredRightToLeft

    inRow.toList ::: inColumn.toList ::: diagonals match {
      case composed if composed.nonEmpty => Some(composed)
      case composed if composed.isEmpty => None
    }
  }

  /**
   * Checks if a list of integers contains at least 4 consecutive integers
   *
   * @param list : list of integers
   * @return
   */
  def isConsecutive(list: Seq[Int], by: Int): Boolean = {
    val consecutiveCount = countConsecutive(list)
    consecutiveCount >= by
  }

  /**
   * Counts the number of consecutive integers in a list of integers.
   * If the list is empty it returns 0. If a series of consecutive integers is interrupted,
   * it starts with 0 again.
   * If it finds a consecutive series of integers that is greater than a max count (e.g. maxCount = 2: 0,1,2,3) it stops and returns the count
   *
   * @param list             : list to check for consecutive integers
   * @param consecutiveCount : counter. default 0
   * @return
   */
  @scala.annotation.tailrec
  private def countConsecutive(list: Seq[Int], consecutiveCount: Int = 0, maxCount: Int = 0): Int = {
    val mCurrentElement = list.headOption
    val tail = if (list.nonEmpty) list.tail else Nil

    (mCurrentElement, tail.headOption) match {
      case (Some(current), Some(next)) =>
        if (current == next - 1) {
          val max = if (consecutiveCount >= maxCount) consecutiveCount + 1 else maxCount
          countConsecutive(tail, consecutiveCount = consecutiveCount + 1, maxCount = max) // Found next consecutive element
        }
        else {
          val max = if (consecutiveCount > maxCount) consecutiveCount else maxCount
          countConsecutive(tail, maxCount = max) // Consecutive streak is interrupted. Start with 0 again
        }
      case _ => maxCount // List is empty or last element in list -> return the consecutive count
    }
  }
}
