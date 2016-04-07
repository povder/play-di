package modules

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{BooksService, CachingBooksService}

class GuiceModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  override def configure() = {
    bind(classOf[BooksService]).to(classOf[CachingBooksService])
  }
}
