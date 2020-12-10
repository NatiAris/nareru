package reviewer

import java.sql.Timestamp

final case class Card(cardId: Long,
                      front: String,
                      back: String,
                      intervalSeconds: Long,
                      dueAt: Timestamp)
