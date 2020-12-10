package controllers

object NewNoteForm {
  import play.api.data.Forms._
  import play.api.data.Form

  case class Data(noteType: String, blob: String)

  val form: Form[Data] = Form(
    mapping(
      "noteType" -> nonEmptyText,
      "blob" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )
}
