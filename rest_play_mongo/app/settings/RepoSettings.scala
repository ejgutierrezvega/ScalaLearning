package settings
import javax.inject.Inject

import play.api.{Configuration, Logger}

class RepoSettings @Inject()(config: Configuration) extends ISettings{

  Logger.info("starting settings")

  val settingsValue =Map(
    REPOSERVER -> config.get[String]("mongo.server"),
    REPOPORT -> config.get[String]("mongo.port"),
    REPODBNAME -> config.get[String]("mongo.db"),
    REPOUSR -> config.get[String]("mongo.user"),
    REPOPWD -> config.get[String]("mongo.pwd")
  )

  override def getSettingValue(key: String): String = settingsValue.get(key).get
}
