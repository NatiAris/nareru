package cardmaker

sealed abstract class Note(val noteType: String) {
  def cards: Iterable[Card]
}

object Note {
  final case class AB(front: String,
                      back: String) extends Note("AB") {
    override val cards: Iterable[Card] = List(
      Card(front, back),
      Card(back, front)
    )
  }

  final case class QA(front: String,
                      back: String) extends Note("QA") {
    override val cards: Iterable[Card] = List(
      Card(front, back)
    )
  }

  final case class Seq(title: String, fields: Vector[String]) extends Note("Seq") {
    override val cards: Vector[Card] = {
      val firstCard = Card(title, fields.head)
      val middleCards = fields.zip(fields.tail).map { case (front, back) => Card(front, back) }
      val lastCard = Card(title, fields.mkString("\n"))
      firstCard +: middleCards :+ lastCard
    }
  }
}
