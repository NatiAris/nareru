package coach

import reviewer.ReviewResult

import java.sql.Timestamp

/**
 * This case class represents a single instance of a card being reviewed and its difficulty evaluated
 */
final case class CardReview(timestamp: Timestamp,
                            reviewResult: ReviewResult)
