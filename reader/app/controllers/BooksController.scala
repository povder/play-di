package controllers

import javax.inject.Inject

import model.Book
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.BooksService

import scalaz._

class BooksController @Inject()(booksService: BooksService) extends Controller {

  def get(id: Int) = Action {
    Book.get(id).map {
      case None => NotFound
      case Some(book) => Ok(Json.toJson(book))
    }.run(booksService)
  }

  def list = Action {
    Book.list().map { books =>
      Ok(Json.toJson(books))
    }.run(booksService)
  }

  def updateTitle(id: Int) = Action(parse.text) { request =>
    Book.get(id).flatMap {
      case Some(book) => book.copy(title = request.body).save().map(_ => NoContent)

      case None => Reader[BooksService, Result](_ => NotFound)
    }.run(booksService)
  }
}
