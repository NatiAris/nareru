package reviewer

sealed trait ReviewResult

object ReviewResult {
  final case object Failure extends ReviewResult
  final case object Hard extends ReviewResult
  final case object Normal extends ReviewResult
  final case object Easy extends ReviewResult

  // enumeratum? ...
  def getResult(name: String): ReviewResult = name match {
    case "Failure" => Failure
    case "Hard" => Hard
    case "Normal" => Normal
    case "Easy" => Easy
  }
}
