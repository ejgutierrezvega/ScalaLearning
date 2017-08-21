package repos

import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{Await, Future}
import scala.util.Failure
import scala.concurrent.duration._
import models._
import play.api.Logger
import reactivemongo.api.QueryOpts
import reactivemongo.bson.BSONDocument

@Singleton
class PersonRepo @Inject()(repoInit: RepoInit) extends IRepo[Person] {

  Logger.info("Creating Person collection")
  val collectionFuture = repoInit.getCollection("persons")
  var personCollection = Await.result(collectionFuture, 10 seconds)

  collectionFuture.onComplete{
    case Failure(e) => e.printStackTrace()
    case _ => Logger.info("Person collection fetched")
  }

  override def get(limit: Int): Future[List[Person]] = {
    val query = BSONDocument()
    val querybuilder = personCollection.find(query)
    querybuilder.options(QueryOpts().batchSize(limit)).cursor[Person]().collect[List](limit)
  }

  override def getById(id: String): Future[Option[Person]] ={
    Logger.info(s"Find by id $id")
    val query = BSONDocument("_id" -> id)
    personCollection.find(query).one[Person]
  }

  override def update(id: String, person: Person) : Boolean = {
    val query = BSONDocument("_id" -> id)

    val writeRes: Future[WriteResult] = personCollection.update(query, person)
    val result = Await.result(writeRes, 10 seconds)

    evaluateWriteResult(result)
  }

  override def save(person: Person): Boolean ={
    Logger.info("Attempt to insert a Person")

    val writeRes: Future[WriteResult] = personCollection.insert(person)
    val result = Await.result(writeRes, 10 seconds)

    evaluateWriteResult(result)
  }

  def evaluateWriteResult(result: WriteResult) : Boolean ={
    result.ok match {
      case false => {
        result.code match{
          case Some(11000) => println("Match the code 11000")
        }
        result.writeErrors.foreach(println(_))
        false
      }
      case _ => {
        Logger.info(result.toString)
        true
      }
    }
  }

}
