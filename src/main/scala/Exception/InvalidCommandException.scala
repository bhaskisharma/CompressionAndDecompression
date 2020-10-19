package Exception

/** Custom Exception representing invalid command  */
final case class InvalidCommandException(message: String) extends Exception(message)
