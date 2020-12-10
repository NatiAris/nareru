package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NoteRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: ExecutionContext) {
  private[models] val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[models] class NotesTable(tag: Tag)
    extends Table[Note](tag, "notes") {
    def noteId = column[Long]("note_id", O.PrimaryKey, O.AutoInc)

    def noteType = column[String]("note_type")

    def * = (noteId, noteType) <> ((Note.apply _).tupled, Note.unapply)
  }

  private[models] val notes = TableQuery[NotesTable]

  def create(note: cardmaker.Note): Future[Note] = db.run {
    (
      notes.map(_.noteType)
        returning notes.map(_.noteId)
        into ((noteType, id) => Note(id, noteType))
      ) += note.noteType
  }

  def list(): Future[Seq[Note]] = db.run {
    notes.result
  }

  def listWithCards(cardRepo: CardRepository): Future[Map[Note, Seq[Card]]] = db.run {
    val cards = cardRepo.cards
    notes.join(cards)
      .on(_.noteId === _.noteId)
      .result
      // ... replace the abomination below with mapN or smth
      .map(_.groupMap { case (n, _) => Note(n.noteId, n.noteType) } { case (_, c) => Card(c.cardId, c.noteId, c.front, c.back) })
  }

  def addNote(cardRepo: CardRepository, note: cardmaker.Note): Unit = {
    // make sure it's done as one transaction ...
    create(note).foreach { case Note(noteId, _) =>
      for { card <- note.cards } {
        cardRepo.create(noteId, card.front, card.back)
      }
    }
  }
}
