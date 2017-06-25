package controllers

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.api.libs.json._
import repos.WidgetRepo
import models.Widget

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object WidgetFields{
  val Id = "_id"
  val Name = "name"
  val Description = "description"
  val Author = "author"
}

class WidgetController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  def repo = new WidgetRepo

  def Get(id: String) = Action { implicit request: Request[AnyContent] =>
    val widget = repo.get(id)

    Ok(Json.toJson[Widget](widget))
  }

  def Post = Action { implicit request: Request[AnyContent] =>
    val bodyWidget: Widget = request.body.asJson.get.validate[Widget].get

    val futureSave = Future { repo.save(bodyWidget) }

    Await.result(futureSave, Duration(10, TimeUnit.SECONDS))

    Created(Json.toJson(bodyWidget))
  }
}
