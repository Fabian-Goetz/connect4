package models

import play.api.libs.json.{Json, OFormat}

case class ChipModel(
                      player: PlayerModel,
                      position: PositionModel,
                    )


object ChipModel {
  implicit val chipModel_format: OFormat[ChipModel] = Json.format[ChipModel]
}