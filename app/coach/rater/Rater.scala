package coach.rater

import reviewer.{Card, ReviewResult}

trait Rater {
  def rate(card: Card, reviewResult: ReviewResult): Card

  val MinimalInterval: Long = 60
}
