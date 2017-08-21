package controllers

import java.util.UUID
import javax.inject._

import play.api.libs.json._
import play.api.mvc._
import repos.PersonRepo
import models._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, personRepo: PersonRepo) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action.async { implicit request: Request[AnyContent] =>
    val personList = personRepo.get(50)

    personList.map { seqOfSomeClass =>
      Ok(Json.toJson(seqOfSomeClass))
    }
  }

  def post() = Action { implicit request: Request[AnyContent] =>
    val person = request.body.asJson.get.validate[Person].get
    val newId = UUID.randomUUID().toString
    val personWithId = person.copy(id = Some(newId))
    personRepo.save(personWithId)

    Created.withHeaders(LOCATION -> s"http://${request.host}${request.path}/${personWithId.id.get}").as(JSON)
  }

  def put(id: String) = Action { implicit request: Request[AnyContent] =>
    val person = request.body.asJson.get.validate[Person].get
    val personWithId = person.copy(id = Some(id))
    val result = personRepo.update(id, personWithId)

    result match{
      case true => Ok(Json.toJson(personWithId)).as(JSON)
      case false => BadRequest
    }
  }

  def personById(id: String) = Action { implicit request: Request[AnyContent] =>

    val futureResult = personRepo.getById(id)

    val person = Await.result(futureResult, 10 seconds)

    Ok(Json.toJson(person))
  }
}
