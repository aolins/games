package controllers

import io.circe._
import io.circe.generic._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._

import io.circe.generic.extras.semiauto._
import io.circe.generic.extras.Configuration

import java.util.UUID
import javax.inject._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}



@Singleton
class GameController  @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


  implicit val customConfig: Configuration = Configuration.default.withKebabCaseMemberNames


  implicit val stateEncoder: Encoder[State] = deriveConfiguredEncoder
  implicit val stateDecoder: Decoder[State] = deriveConfiguredDecoder

  implicit val gtEncoder: Encoder[GameTable] = deriveConfiguredEncoder
  implicit val gtDecoder: Decoder[GameTable] = deriveConfiguredDecoder

  var games:List[GameTable] = List()


  def getGames = Action {
    Ok(games.asJson.noSpaces)
  }

  private def create(s:String): Unit ={
    games = GameTable(UUID.randomUUID().toString, s, Closed) :: games
  }
  def createGame(name:String):Action[AnyContent] = Action{
  create(name)
    Ok("")
  }

  

  def getGame(name:String) = Action{
    Logic.findGame(games,name) match {
      case Some(g) => Ok(g.asJson.noSpaces)
      case _ => NotFound(s"No game with such name $name")
    }
}

  def openGame(name:String) = Action{
    Logic.findGame(games,name) match {
      case Some(x) if x.state == Closed => {
        x.state = Open
        Ok(x.asJson.noSpaces)
      }
      case Some(GameTable(_,_,Open)) =>  BadRequest(s"Game $name is Open; unable to open")
      case _ => NotFound(s"No game with such name $name")
    }
  }


  def removeGame(name:String) = Action{
    Logic.findGame(games,name) match {
      case Some(x) if x.state == Closed => {
        games = games.filter( _ != x)
        Ok("removed")
      }
      case Some(GameTable(_,_,Open)) =>  BadRequest(s"Game $name is Open; unable to remove")
      case _ => NotFound(s"No game with such name $name")

    }
  }

  def closeGame(name:String) = Action{
    Logic.closeGame(games,name) match {
      case Left(x)  => Ok(x.asJson.noSpaces)
      case Right(AttempToCloseClosedGameTable) =>  BadRequest(s"Game $name is Closed; unable to close")
      case Right(NotFound) => NotFound(s"No game with such name $name")
      case _ => BadRequest("Bad logic error")

    }
  }

}

sealed trait State
object Open extends State
object Closed extends State

case class GameTable (id:String, name:String, var state:State)
