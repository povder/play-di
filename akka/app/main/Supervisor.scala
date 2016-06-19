package main

import akka.actor.{Actor, ActorRefFactory, Props}
import javax.inject._
import play.api.Configuration

import services.BooksService

class Supervisor @Inject()(configuration: Configuration) extends Actor {
  val booksService = context.actorOf(BooksService.props, BooksService.name)

  def receive = {
    case _ =>
  }
}

object Supervisor {
  def name = "supervisor"

  def getChild(actorRefFactory: ActorRefFactory, childName: String) =
    actorRefFactory.actorSelection(s"/user/$name/$childName")
}


