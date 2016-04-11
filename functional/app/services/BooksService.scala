package services

import model.Book
import play.api.cache.CacheApi

import scala.collection.mutable
import scala.concurrent.duration._

object BooksService {
  type BSlist = () => Seq[Book]
  type BSget = Int => Option[Book]
  type BSsave = Book => Unit
}

import BooksService._
case class BooksService(list: BSlist, get: BSget, save: BSsave)

object DBBooksService {

  private val db = mutable.Map(
    1 -> Book(1, "Twilight"),
    2 -> Book(2, "50 Shades of Grey"))

  val list: BSlist = () => {
    db.values.toSeq.sortBy(_.id)
  }

  val get: BSget = id => {
    db.get(id)
  }

  val save: BSsave = book => {
    db(book.id) = book
  }
}
