package controllers

import main.Supervisor
import services._
import model.Book

import javax.inject.{Inject, Named}

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import scala.concurrent.duration._
import scala.concurrent.Future

class BooksController @Inject()(system: ActorSystem) extends Controller {
  import system.dispatcher
  implicit val timeout = Timeout(2.seconds)

  val booksService = Supervisor.getChild(system, BooksService.name)

  def get(id: Int) = Action.async {
    (booksService ? BSGet(id)) map {
      case Some(book: Book) =>
        Ok(Json.toJson(book))
      case _ =>
        NotFound
    }
  }

  def list = Action.async {
    (booksService ? BSList) map {
      case books: Seq[Book @unchecked] => Ok(Json.toJson(books))
    }
  }

  def updateTitle(id: Int) = Action.async(parse.text) { request =>
    (booksService ? BSGet(id)) flatMap { case Some(book: Book) =>
      val updatedBook = book.copy(title = request.body)
      (booksService ? BSSave(updatedBook)) map {
        case _ => NoContent
      }
    }
  }
}
