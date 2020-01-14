package utils

import models.BoardModel


trait Observer {
  def update(board: BoardModel): Unit
}

trait Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)

  def notifyObservers(board: BoardModel): Unit = subscribers.foreach(o => o.update(board))
}