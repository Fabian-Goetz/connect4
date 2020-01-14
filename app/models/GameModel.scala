package models

import play.api.libs.json.{Json, OFormat}

case class GameModel(
                      rounds: List[RoundModel] = Nil,
                      players: List[PlayerModel] = Nil,
                      winner: Option[PlayerModel] = None,
                      looser: Option[PlayerModel] = None,
                      startsWith: Option[PlayerModel] = None,
                      isOver: Boolean = false
                    )


object GameModel {
  implicit val gameModel_format: OFormat[GameModel] = Json.format[GameModel]
}


case class CreateGameRequest(
                              name: String,
                              color: String,
                              hasTurn: Boolean
                            )

object CreateGameRequest {
  implicit val createGameModel_format: OFormat[CreateGameRequest] = Json.format[CreateGameRequest]
}