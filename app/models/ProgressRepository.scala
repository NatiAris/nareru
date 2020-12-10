package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProgressRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext) {
  private[models] val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[models] class ProgressTable(tag: Tag)
    extends Table[Progress](tag, "progress") {
    def cardId = column[Long]("card_id")

    def intervalSeconds = column[Long]("interval_seconds")

    def dueAt = column[Timestamp]("due_at")

    def userId = column[Long]("user_id")

    def * = (cardId, intervalSeconds, dueAt, userId) <> ((Progress.apply _).tupled, Progress.unapply)
  }

  private[models] val progress = TableQuery[ProgressTable]

  def create(p: Progress): Future[Int] = db.run {
      progress += p
  }

  def list(): Future[Seq[Progress]] = db.run {
    progress.result
  }
}
