package cardmaker.parsers

import cardmaker.Note

// ... try using Invariant for this
trait NoteParser[+T] {
  def parse(raw: String): Option[T]
}

object NoteParser {
  def parse[T](raw: String)(implicit p: NoteParser[T]): Option[T] = p.parse(raw)

  // This is not supposed to be here
  import LineByLineParsers._

  def parse[T <: Note](noteType: String, raw: String): Option[Note] = noteType match {
    case "AB" => NoteParser[Note.AB].parse(raw)
    case "QA" => NoteParser[Note.QA].parse(raw)
    case "Seq" => NoteParser[Note.Seq].parse(raw)
  }

  def apply[T <: Note](implicit p: NoteParser[T]): NoteParser[T] = p
}
