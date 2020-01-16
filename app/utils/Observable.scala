package utils

import models.{BoardModel, RoundModel}


trait Observer {
  def update(round: RoundModel): Unit
}

trait Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def remove(s: Observer): Unit = subscribers = subscribers.filterNot(o => o == s)

  def notifyObservers(round: RoundModel): Unit = subscribers.foreach(o => o.update(round))
}