import controllers.BooksControllerComponent
import play.api.ApplicationLoader.Context
import play.api._
import services.{CachingBooksServiceComponent, EhCacheComponent}
import router.Routes

class ApplicationLoader extends play.api.ApplicationLoader {
  def load(context: Context) = {
    new ApplicationModule(context).application
  }
}

class ApplicationModule(context: Context)
  extends BuiltInComponentsFromContext(context)
    with BooksControllerComponent
    with CachingBooksServiceComponent
    with EhCacheComponent {

  lazy val router = new Routes(httpErrorHandler, booksController)
}
