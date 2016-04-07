import controllers.BooksController
import play.api.ApplicationLoader.Context
import play.api.{BuiltInComponentsFromContext, BuiltInComponents}
import play.api.cache.EhCacheComponents
import play.api.routing.Router
import services.{BooksService, CachingBooksService}
import router.Routes

class ApplicationLoader extends play.api.ApplicationLoader {
  def load(context: Context) = {
    (new BuiltInComponentsFromContext(context) with ApplicationModule).application
  }
}

trait ApplicationModule extends BuiltInComponents with EhCacheComponents {

  lazy val booksService: BooksService = new CachingBooksService(defaultCacheApi)
  lazy val booksController = new BooksController(booksService)
  lazy val router: Router = new Routes(httpErrorHandler, booksController)
}