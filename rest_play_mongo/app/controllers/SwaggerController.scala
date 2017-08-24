package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

@Singleton
class SwaggerController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def swagger = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.swagger())
  }
}
