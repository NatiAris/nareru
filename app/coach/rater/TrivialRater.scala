package coach.rater

import reviewer.ReviewResult

import java.sql.Timestamp

class TrivialRater extends Rater {
  override def rate(card: reviewer.Card, reviewResult: ReviewResult): reviewer.Card = {
    val multiplier = reviewResult match {
      case ReviewResult.Failure => 0.1
      case ReviewResult.Hard => 1.0
      case ReviewResult.Normal => 2.0
      case ReviewResult.Easy => 4.0
    }
    val newInterval = (card.intervalSeconds * multiplier).toLong max MinimalInterval
    val newDue = new Timestamp(card.dueAt.getTime + newInterval * 1000)
    card.copy(intervalSeconds = newInterval, dueAt = newDue)
  }
}
