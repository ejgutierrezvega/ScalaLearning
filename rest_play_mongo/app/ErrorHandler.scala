import javax.inject._

import play.api._
import play.api.mvc.Results._
import play.api.http.DefaultHttpErrorHandler
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router

import scala.concurrent.Future

import helpers._

@Singleton
class ErrorHandler @Inject() (env: Environment, config: Configuration, sourceMapper: OptionalSourceMapper, router: Provider[Router])
extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  /*
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    val customError = CustomErrorHelper.toCustomError(env, message, "")
    Future.successful(Status(statusCode)(customError))
  }
  */

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    exception match {
      case _ => {
        val customError = CustomErrorHelper.toCustomError(env, exception, "")
        Future.successful(InternalServerError(customError))
      }
    }
  }

  override protected def onNotFound(request: RequestHeader, message: String): Future[Result] = {
    val customError = CustomErrorHelper.toCustomError(env, message, "404")
    Future.successful(NotFound(customError))
  }

  override def onBadRequest(request: RequestHeader, message: String): Future[Result] = {
    val customError = CustomErrorHelper.toCustomError(env, message, "400")
    Future.successful(NotFound(customError))
  }

}
