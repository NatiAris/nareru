package controllers

import models.{CardRepository, NoteRepository, ProgressRepository}
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class BrowserController @Inject()(val controllerComponents: ControllerComponents,
                                  noteRepo: NoteRepository,
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
    noteRepo.listWithCards(cardRepo).map { notes =>
      Ok(views.html.browser(notes))
    }
  }

  def getNotesJson = Action.async { implicit request =>
    noteRepo.list().map { notes =>
      Ok(Json.toJson(notes))
    }
  }

  def getCardsJson = Action.async { implicit request =>
    cardRepo.list().map { cards =>
      Ok(Json.toJson(cards))
    }
  }

  def getProgressJson = Action.async { implicit request =>
    progressRepo.list().map { progress =>
      Ok(Json.toJson(progress))
    }
  }
}
