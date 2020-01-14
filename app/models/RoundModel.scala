package models

import play.api.libs.json.{Json, OFormat}

case class RoundModel(
                     board: BoardModel,
                     players: List[PlayerModel] = Nil,
                     winner: Option[PlayerModel] = None,
                     currentPlayer: Option[PlayerModel] = None,
                     roundNumber: Int = 0,
                     isOver: Boolean = false,
                     winningChips: Option[List[ChipModel]] = None
                   )

object RoundModel {
  implicit val roundModel_format: OFormat[RoundModel] = Json.format[RoundModel]
}