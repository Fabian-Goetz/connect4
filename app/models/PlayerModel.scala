package models

import play.api.libs.json.{Json, OFormat}

case class PlayerModel(
                    name: String,
                    color: String = "black",
                    hasTurn: Boolean = false
                  )

object PlayerModel {
  implicit val playerModel_format: OFormat[PlayerModel] = Json.format[PlayerModel]
}