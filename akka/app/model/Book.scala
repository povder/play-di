package model

import play.api.libs.json.Json

object Book {
  implicit val jsonFormat = Json.format[Book]
}

case class Book(id: Int, title: String)
