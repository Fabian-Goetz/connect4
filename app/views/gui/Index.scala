//package de.htwg.mps.views.gui
//
//import de.htwg.mps.controllers.{GameController, PlayerController, RoundController}
//import de.htwg.mps.utils.Observer
//import javafx.event.ActionEvent
//import scalafx.application.{JFXApp, Platform}
//import scalafx.application.JFXApp.PrimaryStage
//import scalafx.beans.property.{IntegerProperty, StringProperty}
//import scalafx.geometry.Insets
//import scalafx.scene.Scene
//import scalafx.scene.control.{Alert, ButtonType}
//import scalafx.scene.control.Alert.AlertType
//import scalafx.scene.effect.DropShadow
//import scalafx.scene.layout.{GridPane, HBox, Region}
//import scalafx.scene.paint.Color._
//import scalafx.scene.paint.{LinearGradient, Stops}
//import scalafx.scene.shape.Rectangle
//import scalafx.scene.text.Text
//import scalafx.scene.control._
//
//import scala.concurrent.duration.Duration
//import scala.concurrent.{Await, Future}
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.de.htwg.mps.controller.BoardController
//import scala.de.htwg.mps.model.{BoardModel, GameModel, PlayerModel, RoundModel}
//import scala.util.{Failure, Success}
//
//object Index extends JFXApp with Observer {
//  val gameController = new GameController
//  val playerController = new PlayerController
//  val roundController = new RoundController
//  val boardController = new BoardController
//  boardController.add(this)
//  var game: Option[GameModel] = None
//  var isPlayer1Ready = false
//  var isPlayer2Ready = false
//  var currentRound: Option[RoundModel] = None
//  var currentPlayer: Option[PlayerModel] = None
//
//  var grid = new GridPane()
//  grid.layoutX = 20
//  grid.layoutY = 300
//  grid.setManaged(false)
//
//  stage = new PrimaryStage {
//    title = "Connect4"
//    scene = new Scene(700, 700) {
//
//      // Welcome Screen
//      val welcomeLabel = new Label("Welcome")
//      welcomeLabel.layoutX = 20
//      welcomeLabel.layoutY = 20
//      val welcomeButton = new Button("Play Connect4")
//      welcomeButton.layoutX = 20
//      welcomeButton.layoutY = 60
//
//      welcomeButton.onAction = (event: ActionEvent) => {
//        val result = gameController.create.map { g =>
//          welcomeButton.setVisible(false)
//          welcomeLabel.setVisible(false)
//          playerTitle.setManaged(true)
//          namePlayer1.setManaged(true)
//          colorPlayer1.setManaged(true)
//          submitPlayer1.setManaged(true)
//          namePlayer2.setManaged(true)
//          colorPlayer2.setManaged(true)
//          submitPlayer2.setManaged(true)
//          startGame.setManaged(true)
//          game = Some(g)
//        }
//        Await.result(result, Duration.Inf)
//      }
//
//      // Player Screen
//      val playerTitle = new Label("Player")
//      playerTitle.layoutX = 20
//      playerTitle.layoutY = 60
//      playerTitle.setManaged(false)
//      val namePlayer1 = new TextField()
//      namePlayer1.layoutX = 20
//      namePlayer1.layoutY = 100
//      namePlayer1.promptText = "Name Player 1"
//      namePlayer1.setManaged(false)
//      val colorPlayer1 = new TextField()
//      colorPlayer1.layoutX = 200
//      colorPlayer1.layoutY = 100
//      colorPlayer1.promptText = "Color Player 1"
//      colorPlayer1.setManaged(false)
//      val submitPlayer1 = new Button("Submit")
//      submitPlayer1.layoutX = 400
//      submitPlayer1.layoutY = 100
//      submitPlayer1.disable = true
//      submitPlayer1.setManaged(false)
//      val namePlayer2 = new TextField()
//      namePlayer2.layoutX = 20
//      namePlayer2.layoutY = 140
//      namePlayer2.promptText = "Name Player 2"
//      namePlayer2.setManaged(false)
//      val colorPlayer2 = new TextField()
//      colorPlayer2.layoutX = 200
//      colorPlayer2.layoutY = 140
//      colorPlayer2.promptText = "Color Player 1"
//      colorPlayer2.setManaged(false)
//      val submitPlayer2 = new Button("Submit")
//      submitPlayer2.layoutX = 400
//      submitPlayer2.layoutY = 140
//      submitPlayer2.setManaged(false)
//      val startGame = new Button("Start New Game")
//      startGame.layoutX = 20
//      startGame.layoutY = 180
//      startGame.setManaged(false)
//      startGame.disable = true
//
//      namePlayer1.text.onChange {
//        if (namePlayer1.text.apply.length > 0 && colorPlayer1.text.apply.length > 0) submitPlayer1.disable = false else submitPlayer1.disable = false
//      }
//
//      colorPlayer1.text.onChange {
//        if (namePlayer1.text.apply.length > 0 && colorPlayer1.text.apply.length > 0) submitPlayer1.disable = false else submitPlayer1.disable = false
//      }
//
//      submitPlayer1.onAction = (event: ActionEvent) => {
//        playerController.create(game.get, namePlayer1.text.apply, colorPlayer1.text.apply).map { result =>
//          namePlayer1.disable = true
//          colorPlayer1.disable = true
//          submitPlayer1.disable = true
//          result match {
//            case Success(r) =>
//              isPlayer1Ready = true
//              val players = game.get.players :+ r._2
//              game = Some(r._1.copy(players = players))
//              if (isPlayer1Ready && isPlayer2Ready) startGame.disable = false
//
//            case _ => game = None
//          }
//        }
//      }
//
//      namePlayer2.text.onChange {
//        if (namePlayer2.text.apply.length > 0 && colorPlayer2.text.apply.length > 0) {
//          submitPlayer2.disable = false
//        } else {
//          submitPlayer2.disable = false
//        }
//      }
//
//      colorPlayer2.text.onChange {
//        if (namePlayer2.text.apply.length > 0 && colorPlayer2.text.apply.length > 0) {
//          submitPlayer2.disable = false
//        } else {
//          submitPlayer2.disable = false
//        }
//      }
//
//      submitPlayer2.onAction = (event: ActionEvent) => {
//        playerController.create(game.get, namePlayer2.text.apply, colorPlayer2.text.apply).map { result =>
//          namePlayer2.disable = true
//          colorPlayer2.disable = true
//          submitPlayer2.disable = true
//          result match {
//            case Success(r) =>
//              isPlayer2Ready = true
//              val players = game.get.players :+ r._2
//              game = Some(r._1.copy(players = players))
//              if (isPlayer1Ready && isPlayer2Ready) startGame.disable = false
//            case _ => game = None
//          }
//        }
//      }
//
//      startGame.onAction = (event: ActionEvent) => {
//        gameController.addPlayers(game.get, game.get.players).map {
//          case Success(newPlayers) => game = Some(newPlayers)
//            roundController.create(game.get).map { result =>
//              currentRound = Some(result._2)
//              currentPlayer = result._2.currentPlayer
//
//              roundLabel.setManaged(true)
//              roundCounter.setManaged(true)
//              player1Label.setManaged(true)
//              player1Label.setVisible(true)
//              player1Count.setManaged(true)
//              player2Label.setManaged(true)
//              player2Count.setManaged(true)
//              player1Color.setManaged(true)
//              player2Color.setManaged(true)
//              currentPlayerLabel.setManaged(true)
//              currentPlayerName.setManaged(true)
//              grid.setManaged(true)
//
//              playerTitle.setVisible(false)
//              namePlayer1.setVisible(false)
//              colorPlayer1.setVisible(false)
//              submitPlayer1.setVisible(false)
//              namePlayer2.setVisible(false)
//              colorPlayer2.setVisible(false)
//              submitPlayer2.setVisible(false)
//              startGame.setVisible(false)
//
//              Platform.runLater(() => {
//                player1Label.text = namePlayer1.text.apply
//                player2Label.text = namePlayer2.text.apply
//                player1Color.text = colorPlayer1.text.apply
//                player2Color.text = colorPlayer2.text.apply
//                currentPlayerName.text = currentPlayer.get.name
//
//                // Fill grid
//                for (yCoordinate <- 0 until currentRound.get.board.width; xCoordinate <- 0 to currentRound.get.board.height) {
//                  if (xCoordinate == 0) {
//                    val button = new Button((yCoordinate + 1).toString) {
//                      onAction = (event: ActionEvent) => boardController.insertChip(currentRound.get, yCoordinate)
//                    }
//                    grid.add(button, yCoordinate, xCoordinate)
//                  } else {
//                    val emptyCell = new Region {
//                      style = "-fx-background-color: white; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black; -fx-min-width: 20; -fx-min-height:20; -fx-max-width:20; -fx-max-height: 20;"
//                    }
//                    grid.add(emptyCell, yCoordinate, xCoordinate)
//                  }
//                }
//              })
//              result._1
//            }
//          case _ => println("Ups")
//        }
//      }
//
//      // Round
//      val roundLabel = new Label("Round: ")
//      roundLabel.layoutX = 20
//      roundLabel.layoutY = 60
//      roundLabel.setManaged(false)
//      val roundCounter: Label = new Label("0")
//      roundCounter.layoutX = 100
//      roundCounter.layoutY = 60
//      roundCounter.setManaged(false)
//
//      // Players win count
//      val player1Label = new Label("")
//      player1Label.layoutX = 20
//      player1Label.layoutY = 100
//      player1Label.setManaged(false)
//      val player1Color = new Label("")
//      player1Color.layoutX = 100
//      player1Color.layoutY = 100
//      player1Color.setManaged(false)
//      val player1Count = new Label("0")
//      player1Count.layoutX = 200
//      player1Count.layoutY = 100
//      player1Count.setManaged(false)
//      val player2Label = new Label("")
//      player2Label.layoutX = 20
//      player2Label.layoutY = 140
//      player2Label.setManaged(false)
//      val player2Color = new Label("")
//      player2Color.layoutX = 100
//      player2Color.layoutY = 140
//      player2Color.setManaged(false)
//      val player2Count = new Label("0")
//      player2Count.layoutX = 200
//      player2Count.layoutY = 140
//      player2Count.setManaged(false)
//
//      val currentPlayerLabel = new Label("It's your turn: ")
//      currentPlayerLabel.layoutX = 20
//      currentPlayerLabel.layoutY = 200
//      currentPlayerLabel.setManaged(false)
//      val currentPlayerName = new Label("")
//      currentPlayerName.layoutX = 100
//      currentPlayerName.layoutY = 200
//      currentPlayerName.setManaged(false)
//
//
//      // Board
//      content = List(
//        welcomeButton, welcomeLabel,
//        playerTitle, namePlayer1, colorPlayer1, namePlayer2, colorPlayer2, submitPlayer1, submitPlayer2, startGame,
//        roundLabel, roundCounter, player1Label, player1Count, player2Label, player2Count, player1Color, player2Color,
//        currentPlayerLabel, currentPlayerName, grid
//      )
//
//    }
//  }
//
//  override def update(board: BoardModel): Unit = {
//    currentRound = Some(currentRound.get.copy(board = board))
//    updateGrid()
//    println("Neues board", currentRound)
//  }
//
//  def updateGrid() = {
//    grid.setManaged(false)
//    grid = new GridPane()
//
//    // Fill grid
//    for (yCoordinate <- 0 until currentRound.get.board.width; xCoordinate <- 0 to currentRound.get.board.height) {
//      if (xCoordinate == 0) {
//        val button = new Button((yCoordinate + 1).toString) {
//          onAction = (event: ActionEvent) => boardController.insertChip(currentRound.get, yCoordinate)
//        }
//        grid.add(button, yCoordinate, xCoordinate)
//      } else {
//        val exists = currentRound.get.board.chips.exists(c => c.position.x == xCoordinate && c.position.y == yCoordinate)
//        println(exists, currentRound, xCoordinate, yCoordinate)
//        if(exists){
//          val emptyCell = new Region {
//            style = "-fx-background-color: green; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black; -fx-min-width: 20; -fx-min-height:20; -fx-max-width:20; -fx-max-height: 20;"
//          }
//          grid.add(emptyCell, yCoordinate, xCoordinate)
//        } else {
//          val emptyCell = new Region {
//            style = "-fx-background-color: white; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black; -fx-min-width: 20; -fx-min-height:20; -fx-max-width:20; -fx-max-height: 20;"
//          }
//          grid.add(emptyCell, yCoordinate, xCoordinate)
//        }
//      }
//    }
//    grid.setManaged(true)
//  }
//}
