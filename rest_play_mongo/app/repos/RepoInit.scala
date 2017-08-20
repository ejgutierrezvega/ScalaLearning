package repos

import reactivemongo.api.MongoDriver
import reactivemongo.api.MongoConnectionOptions
import reactivemongo.core.nodeset.Authenticate
import settings._
import javax.inject._

import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}
import play.api.inject.ApplicationLifecycle
import reactivemongo.api.collections.bson.BSONCollection

@Singleton
class RepoInit @Inject()(lifeCycle: ApplicationLifecycle, @Named("repoSetting") settings: ISettings) (implicit ec: ExecutionContext) {

  Logger.info("Start repo connection")

  val driver = new MongoDriver
  val options = MongoConnectionOptions()
  val servers = List(s"${settings.getSettingValue(settings.REPOSERVER)}:${settings.getSettingValue(settings.REPOPORT)}")
  val dbname = settings.getSettingValue(settings.REPODBNAME)
  val username = settings.getSettingValue(settings.REPOUSR)
  val password = settings.getSettingValue(settings.REPOPWD)

  val credentials = List(Authenticate(dbname, username, password))

  Logger.info("Creating the connection")
  val conn = driver.connection(servers, options, credentials)

  private def getDatabase(db: String) = conn.database(db)

  def getCollection(collection: String): Future[BSONCollection] = getDatabase(dbname).map(_.collection(collection))

  lifeCycle.addStopHook { () =>
    Logger.info("Closing mongoDB connection")
    Future.successful(conn.close())
  }
}