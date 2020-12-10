package models

import play.api.libs.json.{Json, OFormat}

case class Card(cardId: Long,
                noteId: Long,
                front: String,
                back: String)

object Card {
  implicit val cardFormat: OFormat[Card] = Json.format[Card]
}
