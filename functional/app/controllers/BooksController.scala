package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.BooksService

class BooksController(booksService: BooksService) extends Controller {

  def get(id: Int) = Action {
    booksService.get(id).fold(NotFound: Result) { book =>
      Ok(Json.toJson(book))
    }
  }

  def list = Action {
    Ok(Json.toJson(booksService.list()))
  }

  def updateTitle(id: Int) = Action(parse.text) { request =>
    booksService.get(id) match {
      case Some(book) =>
        booksService.save(book.copy(title = request.body))
        NoContent

      case None => NotFound
    }
  }
}
