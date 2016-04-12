package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import main.Supervisor

class GuiceModule extends AbstractModule with AkkaGuiceSupport {
  override def configure() = {
    bindActor[Supervisor](Supervisor.name)
  }
}
