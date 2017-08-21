package repos

import scala.concurrent.Future

trait IRepo[A] {
  def save(entity: A): Boolean

  def update(id:String, entity:A) : Boolean

  def getById(id : String) : Future[Option[A]]

  def get(limit: Int) : Future[List[A]]
}
