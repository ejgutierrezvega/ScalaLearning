package repos

import javax.inject.Inject

import models.Widget
import helpers.ObservableHelper._
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._

class WidgetRepo @Inject() extends BaseRepo with IRepository[Widget]{

  override protected val collection: MongoCollection[Widget] = database.getCollection("widgets")

  override def save(entity: Widget): String = {
    collection.insertOne(entity).results()
    entity._id
  }

  override def get(id: String): Widget = {
    collection.find(equal("_id", id)).first().headResult()
  }
}
