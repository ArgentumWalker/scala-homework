package ru.spbau.svidchenko.homework0000.calculator.expression.operations

import ru.spbau.svidchenko.homework0000.calculator.expression.Value

/**
  * @author ArgentumWalker
  */
class AdditionTest extends org.scalatest.FunSuite {

  test("calculate simple result") {
    assertResult(5.0)(new Addition(new Value(2.0), new Value(3.0)).calculate())
  }

  test("calculate with inner addition") {
    assertResult(5.0)(
      new Addition(
        new Value(2.0),
        new Addition(
          new Value(1.0),
          new Value(2.0)
        )).calculate())
  }

  test("calculate with negative number") {
    assertResult(5.0)(
      new Addition(
        new Value(2.0),
        new Addition(
          new Value(4.0),
          new Value(-1.0)
        )).calculate())
  }
}
