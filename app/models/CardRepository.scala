package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CardRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: ExecutionContext) {
  private[models] val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[models] class CardsTable(tag: Tag)
    extends Table[Card](tag, "cards") {
    def cardId = column[Long]("card_id", O.PrimaryKey, O.AutoInc)

    def noteId = column[Long]("note_id")

    def front = column[String]("front")

    def back = column[String]("back")

    def * = (cardId, noteId, front, back) <> ((Card.apply _).tupled, Card.unapply)
  }

  private[models] val cards = TableQuery[CardsTable]

  def create(noteId: Long, front: String, back: String): Future[Card] = db.run {
    (
      cards.map(c => (c.noteId, c.front, c.back))
        returning cards.map(_.cardId)
        into { case ((noteId, front, back), cardId) => Card(cardId, noteId, front, back) }
      ) += (noteId, front, back)
  }

  def list(): Future[Seq[Card]] = db.run {
    cards.result
  }
}
