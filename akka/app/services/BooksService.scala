package services

import akka.actor.{Actor, Props}

import scala.collection.mutable
import scala.concurrent.duration._

import model.Book

trait ServiceMsg
case class BSList() extends ServiceMsg
case class BSGet(id: Int) extends ServiceMsg
case class BSSave(book: Book) extends ServiceMsg

class BooksService extends Actor {

  private val books = mutable.Map(
    1 -> Book(1, "Twilight"),
    2 -> Book(2, "50 Shades of Grey"))

  def receive = {
    case BSList =>
      sender ! books.values.toSeq

    case BSGet(id) =>
      sender ! books.get(id)

    case BSSave(book) =>
      books(book.id) = book
      sender ! book
  }
}

object BooksService {
  val props = Props(new BooksService())
  val name = "booksService"
}
