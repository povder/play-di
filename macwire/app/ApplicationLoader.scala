import controllers.BooksController
import play.api.ApplicationLoader.Context
import play.api._
import play.api.cache.{CacheApi, EhCacheComponents}
import play.api.routing.Router
import services.{BooksService, CachingBooksService}
import router.Routes

class ApplicationLoader extends play.api.ApplicationLoader {
  def load(context: Context) = {
    (new BuiltInComponentsFromContext(context) with ApplicationModule).application
  }
}

trait ApplicationModule extends BuiltInComponents with EhCacheComponents {

  import com.softwaremill.macwire._

  lazy val cacheApi: CacheApi = defaultCacheApi
  lazy val booksService: BooksService = wire[CachingBooksService]
  lazy val booksController = wire[BooksController]

  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
}
