package settings

trait ISettings {

  val REPOSERVER = "server"
  val REPOPORT = "port"
  val REPODBNAME = "dbName"
  val REPOUSR = "user"
  val REPOPWD = "pwd"


  def getSettingValue(key: String) : String
}
