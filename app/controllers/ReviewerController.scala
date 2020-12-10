package controllers

import models.{Card, CardRepository, ProgressRepository}
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ReviewerController @Inject()(val controllerComponents: ControllerComponents,
                                   cardRepo: CardRepository,
                                   progressRepo: ProgressRepository) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action.async { implicit request: Request[AnyContent] =>
    // ... remove hardcoded userid
    cardRepo.getDueCard(progressRepo, userId = 1).map {
      case Some((cardId, noteId, front, back)) =>
        Ok(views.html.reviewer(Some(Card(cardId, noteId, front, back))))
      case None =>
        Ok(views.html.reviewer(None))
    }
  }

  def reviewCard = Action.async { implicit request =>
    Future.successful(Ok(""))
  }
}
