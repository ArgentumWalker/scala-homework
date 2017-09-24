package ru.spbau.svidchenko.homework0000.calculator.expression.operations

import org.scalatest.FunSuite
import ru.spbau.svidchenko.homework0000.calculator.expression.Value

/**
  * Created by ArgentumWalker on 09.09.2017.
  */
class DivisionTest extends FunSuite {

  test("calculate simple result") {
    assertResult(5.0)(new Division(new Value(10.0), new Value(2.0)).calculate())
  }

  test("calculate with inner division") {
    assertResult(5.0)(
      new Division(
        new Value(10.0),
        new Division(
          new Value(4.0),
          new Value(2.0)
        )).calculate())
  }

  test("calculate with negative number") {
    assertResult(-5.0)(
      new Division(
        new Value(-10.0),
        new Division(
          new Value(4.0),
          new Value(2.0)
        )).calculate())
  }
}
