package controllers

import javax.inject._
import models.{CreateGameRequest, InsertChipRequest}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AnyContent, ControllerComponents, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RoutingController @Inject()(controllerComponents: ControllerComponents,
                                  gameController: GameController,
                                  boardController: BoardController
                                 ) extends AbstractController(controllerComponents) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }


  /**
   * Creates a game from a list of player names and colors
   *
   * @return
   */
  def createGame: Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson.map(_.validate[Seq[CreateGameRequest]] match {
      case JsSuccess(validRequest, _) =>
        gameController.create(validRequest).map {
          case Success(value) => Ok(Json.obj("game" -> value))
          case Failure(exception) => BadRequest(exception.getMessage)
        }
      case e: JsError => Future.successful(BadRequest(e.getOrElse("")))
    }).getOrElse(Future.successful(InternalServerError("")))
  }

  /**
   * Inserts a chip into a certain column
   *
   * @return
   */
  def insertChip(): Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson.map(_.validate[InsertChipRequest] match {
      case JsSuccess(validRequest, _) =>
        boardController.insertChip(validRequest.round, validRequest.column).map {
          case Success(value) => Ok(Json.obj("game" -> value))
          case Failure(exception) => BadRequest(exception.getMessage)
        }
      case e: JsError => Future.successful(BadRequest(e.getOrElse("")))
    }).getOrElse(Future.successful(InternalServerError("")))
  }
}
