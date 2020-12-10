package models

import play.api.libs.json.{Json, OFormat}

case class Note(noteId: Long,
                noteType: String)

object Note {
  implicit val noteFormat: OFormat[Note] = Json.format[Note]
}
