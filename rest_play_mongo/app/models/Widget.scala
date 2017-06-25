package models

import generators.uidGenerator
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Widget(_id: String, name: String, description: String, author: String)

object Widget{
  def apply(_id: String, name: String, description: String, author: String): Widget =
    new Widget(uidGenerator.random.toString, name, description, author)

  implicit val widgetReads: Reads[Widget] =(
      (JsPath \ "id").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "author").read[String]
  )(Widget.apply(_:String,_:String,_:String,_:String))

  implicit val widgetWrites: Writes[Widget] =(
    (JsPath \ "id").write[String] and
      (JsPath \ "name").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "author").write[String]
  )(unlift(Widget.unapply))
}
