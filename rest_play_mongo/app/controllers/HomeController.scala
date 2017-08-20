package controllers

import java.util.UUID
import javax.inject._

import play.api.libs.json._
import play.api.mvc._
import repos.PersonRepo
import models._

import scala.concurrent.Await
import scala.concurrent.duration._

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
  def index() = Action { implicit request: Request[AnyContent] =>

    val newId = UUID.randomUUID().toString
    val person = new Person(newId, "Edgar", "Gutierrez", 34)
    personRepo.save(person)

    Ok(Json.toJson(person))
  }

  def personById(id: String) = Action { implicit request: Request[AnyContent] =>

    val futureResult = personRepo.getById(id)

    val person = Await.result(futureResult, 10 seconds)

    Ok(Json.toJson(person))
  }
}
