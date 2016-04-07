package services

import model.Book
import play.api.cache.CacheApi

import scala.collection.mutable
import scala.concurrent.duration._

trait BooksService {
  def list: Seq[Book]

  def get(id: Int): Option[Book]

  def save(book: Book): Unit
}

trait BooksServiceComponent {
  def booksService: BooksService
}

class CachingBooksService(cache: CacheApi) extends BooksService {

  private val books = mutable.Map(
    1 -> Book(1, "Twilight"),
    2 -> Book(2, "50 Shades of Grey"))

  private def fetchFreshBooks(): Seq[Book] = {
    books.values.toSeq
  }

  override def list: Seq[Book] = {
    cache.getOrElse("books") {
      def freshBooks = fetchFreshBooks()
      cache.set("books", freshBooks, 2.minutes)
      freshBooks
    }
  }

  override def get(id: Int): Option[Book] = {
    books.get(id)
  }

  override def save(book: Book): Unit = {
    books(book.id) = book
  }
}

trait CachingBooksServiceComponent extends BooksServiceComponent {
  this: CacheComponent =>

  lazy val booksService = new CachingBooksService(defaultCacheApi)
}

