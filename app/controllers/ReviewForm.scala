package controllers

object ReviewForm {
  import play.api.data.Forms._
  import play.api.data.Form

  case class Data(cardId: Long, userId: Long, result: String)

  val form: Form[Data] = Form(
    mapping(
      "cardId" -> longNumber,
      "userId" -> longNumber,
      "result" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
}
