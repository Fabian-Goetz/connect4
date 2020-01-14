package models

import play.api.libs.json.{Json, OFormat}

case class BoardModel(
                       chips: List[ChipModel] = Nil,
                       width: Int = 7,
                       height: Int = 6
                     )

object BoardModel {
  implicit val boardModel_format: OFormat[BoardModel] = Json.format[BoardModel]
}


case class InsertChipRequest(
                              round: RoundModel,
                              column: Int
                            )

object InsertChipRequest {
  implicit val insertChipModel_format: OFormat[InsertChipRequest] = Json.format[InsertChipRequest]
}
