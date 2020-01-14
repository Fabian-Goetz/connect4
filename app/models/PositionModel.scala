package models

import play.api.libs.json.{Json, OFormat}

case class PositionModel(
                     x: Int = 0,
                     y: Int = 0
                   )

object PositionModel {
  implicit val positionModel_format: OFormat[PositionModel] = Json.format[PositionModel]
}
