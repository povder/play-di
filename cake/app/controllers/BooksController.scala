package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.{BooksService, BooksServiceComponent}

trait BooksControllerComponent {
  this: BooksServiceComponent =>

  lazy val booksController = new BooksController(booksService)
}

class BooksController(booksService: BooksService) extends Controller {

  def get(id: Int) = Action {
    booksService.get(id).fold(NotFound: Result) { book =>
      Ok(Json.toJson(book))
    }
  }

  def list = Action {
    def books = booksService.list
    Ok(Json.toJson(books))
  }

  def updateTitle(id: Int) = Action(parse.text) { request =>
    booksService.get(id).fold(NotFound: Result) { book =>
      val updatedBook = book.copy(title = request.body)
      booksService.save(updatedBook)
      NoContent
    }
  }
}
