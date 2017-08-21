import com.google.inject.AbstractModule
import com.google.inject.name.Names
import generators.{IGenerator, uidGenerator}
import models.Person
import repos.{IRepo, PersonRepo, RepoInit}
import settings.{ISettings, RepoSettings}

class Module extends AbstractModule {
  def configure() = {

    bind(classOf[IGenerator])
      .to(classOf[uidGenerator]).asEagerSingleton()

    bind(classOf[ISettings])
      .annotatedWith(Names.named("repoSetting"))
      .to(classOf[RepoSettings]).asEagerSingleton()

    bind(classOf[RepoInit]).asEagerSingleton()

    bind(classOf[IRepo[Person]])
      //.annotatedWith(Names.named("personRepo"))
      .to(classOf[PersonRepo]).asEagerSingleton()
  }
}