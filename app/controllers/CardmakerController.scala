package controllers

import cardmaker.parsers.NoteParser
import models.{CardRepository, NoteRepository}
import play.api.mvc._

import javax.inject._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CardmakerController @Inject()(noteRepo: NoteRepository,
                                    cardRepo: CardRepository,
                                    cc: MessagesControllerComponents)
  extends MessagesAbstractController(cc) {

  val noteForm = NewNoteForm.form

  def index = Action { implicit request =>
    Ok(views.html.cardmaker(noteForm))
  }

  def addNote = Action.async { implicit request =>
    noteForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.cardmaker(errorForm)))
      },
      note => {
        val maybeNote = NoteParser.parse(note.noteType, note.blob)
        Future.successful(
          // Consider moving this piece of logic above
          maybeNote match {
            case None => BadRequest("Couldn't parse the text")
            case Some(note) => Ok {
              noteRepo.addNote(cardRepo, note)
              "Successful"
            }
          }
        )
      }
    )


  }
}
