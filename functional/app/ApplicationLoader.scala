import controllers.BooksController
import play.api.ApplicationLoader.Context
import play.api.{BuiltInComponentsFromContext, BuiltInComponents}
import play.api.cache.{CacheApi, EhCacheComponents}
import play.api.routing.Router
import scala.concurrent.duration._
import scala.reflect.ClassTag
import services.{BooksService, BSTypes, DBBooksService}
import router.Routes

class ApplicationLoader extends play.api.ApplicationLoader {
  def load(context: Context) = {
    (new BuiltInComponentsFromContext(context) with ApplicationModule).application
  }
}

trait ApplicationModule extends BuiltInComponents with EhCacheComponents {
  import DBBooksService._
  import Cache._
  import Log._

  val dbBooksService = BooksService(list, get, save)

  val cachedList: BSTypes.BSlist = () => cacheFn(defaultCacheApi)(s"books") {
    list()
  }
  val cachedGet: BSTypes.BSget = id => cacheFn(defaultCacheApi)(s"book$id") {
    get(id)
  }
  val cachedBooksService = BooksService(cachedList, cachedGet, save)

  val loggedCachedBooksService = BooksService(
    () => log("List") { cachedList() },
    id => log(s"Get($id)") { cachedGet(id) },
    book => log(s"Save($book)") { save(book) })

  val booksController = new BooksController(loggedCachedBooksService)
  lazy val router: Router = new Routes(httpErrorHandler, booksController)
}

object Cache {
  def cacheFn[T: ClassTag](cache: CacheApi)
    (key: String, duration: Duration = 2.minutes)(fn: => T): T = {
    cache.getOrElse(key) {
      val result = fn
      cache.set(key, result, duration)
      result
    }
  }
}

object Log {
  def log[T](message: String)(fn: => T): T = {
    val start = System.nanoTime
    val result = fn
    val took = (System.nanoTime - start) / 1000
    play.Logger.debug(s"$message => $result [$took micros]")
    result
  }
}
