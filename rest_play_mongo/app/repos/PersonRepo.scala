package repos

import java.util.UUID
import javax.inject._

import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.api.commands.WriteResult

import scala.concurrent.{Await, Future}
import scala.util.Failure
import scala.concurrent.duration._
import models._
import play.api.Logger
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

  override def getById(id: String): Future[Option[Person]] ={

    val query = BSONDocument("_id" -> id)

    val futureResult = personCollection.find(query).one[BSONDocument]

    val result = Await.result(futureResult, 10 seconds)

    Future.successful(Some(new Person(UUID.randomUUID().toString, "Tania", "Estrada", 30)))
  }

  override def save(person: Person): Unit ={
    Logger.info("Attempt to insert a Person")

    val writeRes: Future[WriteResult] = personCollection.insert(person)
    val result = Await.result(writeRes, 10 seconds)

    result.ok match {
      case false => {
        result.code match{
          case WriteResult.Code(11000) => println("Match the code 11000")
          case WriteResult.Message("Must match this exact message") => println("Match the code 11000")
        }
        result.writeErrors.foreach(println(_))
        Future.successful(false)
      }
      case _ => {
        Logger.info(result.toString)
        Future.successful(true)
      }
    }
  }
}
