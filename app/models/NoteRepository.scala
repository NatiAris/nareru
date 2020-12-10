package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NoteRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class NotesTable(tag: Tag) extends Table[Note](tag, "notes") {
    def noteId = column[Long]("note_id", O.PrimaryKey, O.AutoInc)
    def noteType = column[String]("note_type")

    def * = (noteId, noteType) <> ((Note.apply _).tupled, Note.unapply)
  }

  private val notes = TableQuery[NotesTable]

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
}
