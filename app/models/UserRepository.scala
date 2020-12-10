package models

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)
                              (implicit ec: ExecutionContext) {
  private[models] val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private[models] class UsersTable(tag: Tag)
    extends Table[User](tag, "users") {
    def userId = column[Long]("user_id")
    def login = column[String]("login")
    def password = column[String]("password")

    def * = (userId, login, password) <> ((User.apply _).tupled, User.unapply)
  }

  private[models] val users = TableQuery[UsersTable]

  def create(login: String, password: String): Future[User] = db.run {
    (
      users.map(u => (u.login, u.password))
      returning users.map(_.userId)
      into { case ((login, password), userId) => User(userId, login, password) }
    ) += (login, password)
  }

  def list(): Future[Seq[User]] = db.run {
    users.result
  }

}


// case class User(userId: Long,
//                 login: String,
//                 password: String)
