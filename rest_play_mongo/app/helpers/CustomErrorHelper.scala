package helpers

import java.io.{StringWriter, PrintWriter}

import play.api.Environment
import play.api.libs.json._
import play.api.Mode

object CustomErrorHelper {

  def toCustomError(env: Environment, exception: Throwable, errorCode: String) : JsValue ={
    val sw = new StringWriter()
    exception.printStackTrace(new PrintWriter(sw))

    env.mode match {
      case Mode.Prod => {
        val customException = Map(
          "errorcode" -> errorCode,
          "description" -> exception.getMessage
        )
        Json.toJson(customException)
      }
      case _ => {
        val customException = Map(
          "errorcode" -> errorCode,
          "description" -> exception.getMessage,
          "stacktrace" -> sw.toString
        )
        Json.toJson(customException)
      }
    }

  }

  def toCustomError(env: Environment, exception: String, errorCode: String) : JsValue ={
    env.mode match {
      case Mode.Prod => {
        val customException = Map(
          "errorcode" -> errorCode,
          "description" -> exception
        )
        Json.toJson(customException)
      }
      case Mode.Dev => {
        val customException = Map(
          "errorcode" -> errorCode,
          "description" -> exception,
          "stacktrace" -> ""
        )
        Json.toJson(customException)
      }
    }
  }
}
