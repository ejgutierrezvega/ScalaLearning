package filters

import javax.inject._

import akka.stream.Materializer
import play.api.{Configuration, Logger}
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RequestFilter @Inject() (implicit override val mat: Materializer, exec: ExecutionContext, config: Configuration) extends Filter {

  override def apply(nextFilter: RequestHeader => Future[Result])
                    (requestHeader: RequestHeader) : Future[Result] ={
    val startTime = System.currentTimeMillis()

    nextFilter(requestHeader).map { result =>
      val endtime = System.currentTimeMillis()
      val requestTime = endtime - startTime

      requestHeader.attrs.contains(Router.Attrs.HandlerDef) match {
        case true => {
          val handlerDef = requestHeader.attrs(Router.Attrs.HandlerDef)
          Logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
          Logger.info(s"${handlerDef.controller} took ${requestTime}ms and returned ${result.header.status}")
        }
        case _ => {
          Logger.info(s"${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and resturned ${result.header.status}")
        }
      }

      result.withHeaders("Request-Time" -> requestTime.toString)

    }
  }

}
