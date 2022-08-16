package controllers

import java.util.UUID

import javax.inject._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}



@Singleton
class GameController  @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  var games:List[GameTable] = List()


  def getGames = Action {
    Ok(games.toString())
  }

  private def create(s:String): Unit ={
    games = GameTable(UUID.randomUUID().toString, s, Closed) :: games
  }
  def createGame(name:String):Action[AnyContent] = Action{
  create(name)
    Ok("")
  }

  private def findGame(name:String) = games.find( p => p.name == name)

  def getGame(name:String) = Action{
    findGame(name) match {
      case Some(g) => Ok(g.toString)
      case _ => NotFound(s"No game with such name $name")
    }
}

  def openGame(name:String) = Action{
    findGame(name) match {
      case Some(x) if x.state == Closed => {
        x.state = Open
        Ok("opened")
      }
      case Some(GameTable(_,_,Open)) =>  BadRequest(s"Game $name is Open; unable to open")
      case _ => NotFound(s"No game with such name $name")
    }
  }


  def removeGame(name:String) = Action{
    findGame(name) match {
      case Some(x) if x.state == Closed => {
        games = games.filter( _ != x)
        Ok("removed")
      }
      case Some(GameTable(_,_,Open)) =>  BadRequest(s"Game $name is Open; unable to remove")
      case _ => NotFound(s"No game with such name $name")

    }
  }

  def closeGame(name:String) = Action{
    findGame(name) match {
      case Some(x) if x.state == Open => {
        x.state = Closed
        Ok("closed")
      }
      case Some(GameTable(_,_,Closed)) =>  BadRequest(s"Game $name is Closed; unable to close")
      case _ => NotFound(s"No game with such name $name")

    }
  }

}

trait State
object Open extends State
object Closed extends State

case class GameTable (id:String, name:String, var state:State)
