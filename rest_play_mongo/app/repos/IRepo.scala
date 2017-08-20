package repos

import scala.concurrent.Future

trait IRepo[A] {
  def save(entity: A): Unit

  def getById(id : String) : Future[Option[A]]
}
