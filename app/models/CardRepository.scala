package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp

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

  def getDueCard(progressRepo: ProgressRepository, userId: Long) = db.run {
//    val reviews = progressRepo.progress.filter(_.userId === userId).result
//    for {
//      (c, p) <- cards joinLeft progressRepo.progress on(_.cardId === _.cardId)
//    } yield (c.cardId, c.front, c.back, p.dueAt, p.intervalSeconds)
//    val progress = progressRepo.progress
//    for {
////      Card(cardId, noteId, front, back) <- cards
////      Progress(cardId, intervalSeconds, dueAt, userId) <- progress
//      c <- cards.map(c => (c.cardId, c.front, c.back))
//      p <- progress.map(p => (p.cardId, p.intervalSeconds, p.dueAt, p.userId))
//      if (c._1 === p._1)
//      if (p.userId === userId)
////      if (p.dueAt > new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//    } yield (reviewer.Card(c.cardId, c.front, c.back, p.intervalSeconds, p.dueAt))
    // ... replace with a proper query
    sql"""
          SELECT c.card_id,
                 c.note_id,
                 c.front,
                 c.back
            FROM cards c
            LEFT JOIN progress p ON c.card_id = p.card_id
           WHERE p.user_id = $userId OR p.user_id is NULL
       """.as[(Long, Long, String, String)]
      .headOption
  }
}
