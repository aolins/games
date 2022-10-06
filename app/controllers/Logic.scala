package controllers

import play.api.http.Status


sealed trait LogicError
object NotFound extends LogicError
object AttempToCloseClosedGameTable extends LogicError
object AttempToOpenOpenedGameTable extends LogicError

  object Logic {


  def findGame(games: List[GameTable], name:String) = games.find( p => p.name == name)



  def closeGame(games: List[GameTable], name:String): Either[GameTable, LogicError] = {
    findGame(games, name) match {
      case Some(x) if x.state == Open => {
        x.state = Closed
        Left(x)
      }
      case Some(GameTable(_, _, Closed)) => Right(AttempToCloseClosedGameTable)
      case _ => Right(NotFound)
    }
  }
}
