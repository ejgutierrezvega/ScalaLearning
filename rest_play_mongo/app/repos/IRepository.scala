package repos

import org.mongodb.scala._

trait IRepository[A] {
  protected val collection: MongoCollection[A]

  def save(entity: A): String
  def get(id: String): A
}
