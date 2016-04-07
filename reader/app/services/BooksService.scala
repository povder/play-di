package services

import javax.inject.Inject

import model.Book
import play.api.cache.CacheApi

import scala.collection.mutable
import scala.concurrent.duration._

trait BooksService {
  def list: Seq[Book]

  def get(id: Int): Option[Book]

  def save(book: Book): Unit
}

class CachingBooksService @Inject()(cache: CacheApi) extends BooksService {

  private val db = mutable.Map(
    1 -> Book(1, "Twilight"),
    2 -> Book(2, "50 Shades of Grey")) //simulates some persistent storage

  override def list: Seq[Book] = {
    cache.getOrElse("books") {
      //get "books" entry from cache, if it doesn't exist fetch fresh list from the "DB"
      def freshBooks = fetchFreshBooks()
      cache.set("books", freshBooks, 2.minutes) //cache freshly fetched books for 2 minutes
      freshBooks
    }
  }

  override def get(id: Int): Option[Book] = {
    cache.getOrElse(s"book$id") {
      def freshBook = fetchFreshBook(id)
      cache.set(s"book$id", freshBook, 2.minutes)
      freshBook
    }
  }

  override def save(book: Book): Unit = {
    db(book.id) = book
  }

  private def fetchFreshBooks(): Seq[Book] = {
    db.values.toSeq.sortBy(_.id)
  }

  private def fetchFreshBook(id: Int): Option[Book] = {
    db.get(id)
  }

}
