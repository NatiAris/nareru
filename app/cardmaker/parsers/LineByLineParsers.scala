package cardmaker.parsers

import cardmaker.Note
import cardmaker.Note.{AB, QA, Seq}

object LineByLineParsers {
  implicit val noteParserAB: NoteParser[AB] = (raw: String) => {
    val lines = raw.split('\n').toVector
    lines match {
      case first +: rest => Some(AB(first, rest.mkString("\n")))
      case _ => None
    }
  }

  implicit val noteParserQA: NoteParser[QA] = (raw: String) => {
    val lines = raw.split('\n').toVector
    lines match {
      case first +: rest => Some(QA(first, rest.mkString("\n")))
      case _ => None
    }
  }

  implicit val noteParserSeq: NoteParser[Seq] = (raw: String) => {
    val lines = raw.split('\n').toVector
    lines match {
      case first +: rest => Some(Seq(first, rest))
      case _ => None
    }
  }
}
