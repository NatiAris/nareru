package models

import play.api.libs.json.{Format, JsObject, JsResult, JsValue, Json, OFormat}

import java.sql.Timestamp

case class Progress(cardId: Long,
                    intervalSeconds: Long,
                    dueAt: Timestamp,
                    userId: Long)

object Progress {
  implicit val timestampFormat: Format[Timestamp] = new Format[Timestamp] {
    override def reads(json: JsValue): JsResult[Timestamp] = Json.fromJson[Long](json).map(new Timestamp(_))
    override def writes(o: Timestamp): JsValue = Json.toJson(o.getTime)
  }
  implicit val progressFormat: OFormat[Progress] = Json.format[Progress]
}
