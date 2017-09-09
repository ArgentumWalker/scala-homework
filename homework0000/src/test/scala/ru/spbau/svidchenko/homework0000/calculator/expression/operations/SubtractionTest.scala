package ru.spbau.svidchenko.homework0000.calculator.expression.operations

import org.scalatest.FunSuite
import ru.spbau.svidchenko.homework0000.calculator.expression.Value

/**
  * @author ArgentumWalker
  */
class SubtractionTest extends FunSuite {

  test("calculate simple result") {
    assertResult(5.0)(new Subtraction(new Value(6.0), new Value(1.0)).calculate())
  }

  test("calculate with inner subtraction") {
    assertResult(5.0)(
      new Subtraction(
        new Value(6.0),
        new Subtraction(
          new Value(2.0),
          new Value(1.0)
        )).calculate())
  }

  test("calculate with negative number") {
    assertResult(5.0)(
      new Subtraction(
        new Value(5.0),
        new Subtraction(
          new Value(-4.0),
          new Value(-4.0)
        )).calculate())
  }
}
