package ru.spbau.svidchenko.homework0000.calculator.expression.operations

import org.scalatest.FunSuite
import ru.spbau.svidchenko.homework0000.calculator.expression.Value

/**
  * @author ArgentumWalker
  */
class MultiplicationTest extends FunSuite {

  test("calculate simple result") {
    assertResult(6.0)(new Multiplication(new Value(2.0), new Value(3.0)).calculate())
  }

  test("calculate with inner multiplication") {
    assertResult(12.0)(
      new Multiplication(
        new Value(2.0),
        new Multiplication(
          new Value(3.0),
          new Value(2.0)
        )).calculate())
  }

  test("calculate with negative number") {
    assertResult(-6.0)(
      new Multiplication(
        new Value(2.0),
        new Multiplication(
          new Value(3.0),
          new Value(-1.0)
        )).calculate())
  }
}
