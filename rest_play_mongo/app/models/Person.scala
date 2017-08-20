package models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONNumberLike, BSONDocumentWriter}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import reactivemongo.bson.Macros.Annotations.Key

case class Person (@Key("_id") id: String, firstName: String, lastName: String, age: Int)

object Person {

  implicit val PersonJsonWriter = new Writes[Person] {
    def writes(person: Person) = Json.obj(
      "id" -> person.id,
      "firstName" -> person.firstName,
      "lastName" -> person.lastName,
      "age" -> person.age
    )
  }

  implicit val PersonJsonReader: Reads[Person] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "firstName").read[String] and
    (JsPath \ "lastName").read[String] and
    (JsPath \ "age").read[Int]
    )(Person.apply _)

  implicit object PersonBsonReader extends BSONDocumentReader[Person] {
    def read(bson: BSONDocument): Person = {
      val opt: Option[Person] = for {
        id <- bson.getAs[String]("_id")
        name <- bson.getAs[String]("firstName")
        last <- bson.getAs[String]("lastName")
        age <- bson.getAs[BSONNumberLike]("personAge").map(_.toInt)
      } yield new Person(id, name, last, age)

      opt.get // the person is required (or let throw an exception)
    }
  }

  implicit object PersonBsonWriter extends BSONDocumentWriter[Person] {
    def write(person: Person): BSONDocument =
      BSONDocument(
        "_id" -> person.id
        ,"firstName" -> person.firstName
        , "lastName" -> person.lastName
        , "age" -> person.age)
  }

}