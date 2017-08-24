package controllers

import javax.inject._

import generators.IGenerator
import io.swagger.annotations._
import play.api.libs.json._
import play.api.mvc._
import repos.PersonRepo
import models._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

@Api(value = "/Persons")
class PersonController @Inject()(cc: ControllerComponents, personRepo: PersonRepo, uidGenerator: IGenerator) extends AbstractController(cc) {

  @ApiOperation(
    nickname = "getAllPersons",
    value = "Get all persons from repository",
    notes = "Return all persons",
    response = classOf[List[Person]],
    httpMethod = "Get"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Person updated"),
    new ApiResponse(code = 400, message = "Bad request")
  ))
  def index = Action.async { implicit request: Request[AnyContent] =>
    val personList = personRepo.get(50)

    personList.map { persons =>
      Ok(Json.toJson(persons))
    }
  }

  @ApiOperation(
    nickname = "postPerson",
    value = "Save a person to repository",
    notes = "Save a person",
    httpMethod = "Post"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Person updated"),
    new ApiResponse(code = 400, message = "Bad request")
  ))
  def post = Action { implicit request: Request[AnyContent] =>
    val person = request.body.asJson.get.validate[Person].get
    val newId = uidGenerator.random.toString
    val personWithId = person.copy(id = Some(newId))
    personRepo.save(personWithId)

    Created.withHeaders(LOCATION -> s"http://${request.host}${request.path}/${personWithId.id.get}").as(JSON)
  }

  @ApiOperation(
    nickname = "putPerson",
    value = "Updates a person to repository",
    notes = "Updates a person",
    response = classOf[Person],
    httpMethod = "Put"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Person updated"),
    new ApiResponse(code = 400, message = "Bad request")
  ))
  @ApiImplicitParams(Array(
    new ApiImplicitParam (name = "person", value = "Person entity", required = true, dataType = "models.Person", paramType = "body")
  ))
  def put(@ApiParam(value = "Person id") id: String) = Action { implicit request: Request[AnyContent] =>
    val person = request.body.asJson.get.validate[Person].get
    val personWithId = person.copy(id = Some(id))
    val result = personRepo.update(id, personWithId)

    result match{
      case true => Ok(Json.toJson(personWithId)).as(JSON)
      case false => BadRequest
    }
  }

  @ApiOperation(
    nickname = "getPersonById",
    value = "Get a person from repository by id",
    notes = "Get a person by id",
    response = classOf[Person],
    httpMethod = "Get"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Person updated"),
    new ApiResponse(code = 400, message = "Bad request")
  ))
  def personById(@ApiParam(value = "Person id") id: String) = Action { implicit request: Request[AnyContent] =>

    val futureResult = personRepo.getById(id)

    val person = Await.result(futureResult, 10 seconds)

    person match {
      case None => throw new Exception("Id not found.")
      case _ => Ok(Json.toJson(person))
    }
}
}
