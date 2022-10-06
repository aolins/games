package controllers

import org.scalatest.{FlatSpec, Matchers}

class GameControllerTest extends FlatSpec with Matchers {


  "Empty " should " give None" in {
    val g = List()
    Logic.findGame(g,"") shouldBe None
  }


  "Some " should " give Some" in {

    val gt = GameTable("","name", Open)
    val g = List(gt)
    Logic.findGame(g, "name") shouldBe Some(gt)
  }

  "Not found " should " not allow closing" in {
    val gt = GameTable("", "name", Open)
    val g = List(gt)
    Logic.closeGame(g, "other") shouldBe Right(NotFound)
  }


  "Bad request " should " not allow closing" in {
    val gt = GameTable("", "name", Closed)
    val g = List(gt)
    Logic.closeGame(g, "name") shouldBe Right(AttempToCloseClosedGameTable)
  }
}
