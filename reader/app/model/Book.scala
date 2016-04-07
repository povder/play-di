package model

import play.api.libs.json.Json
import services.BooksService

import scalaz._

object Book {
  implicit val jsonFormat = Json.format[Book]

  def get(id: Int) = Reader[BooksService, Option[Book]] { service =>
    service.get(id)
  }

  def list() = Reader[BooksService, Seq[Book]] { service =>
    service.list
  }
}

case class Book(id: Int, title: String) {

  def save() = Reader[BooksService, Unit] { service =>
    service.save(this)
  }
}
