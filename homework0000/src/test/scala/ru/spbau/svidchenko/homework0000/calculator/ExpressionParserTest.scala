package ru.spbau.svidchenko.homework0000.calculator

import org.scalatest.FunSuite

/**
  * @author ArgentumWalker
  */
class ExpressionParserTest extends FunSuite {

  test("value") {
    assertResult(12.0)(ExpressionParser.parse("12.0").calculate())
  }

  test("simple operations") {
    assertResult(12.0)(ExpressionParser.parse("add(6.0, 6.0)").calculate())
    assertResult(36.0)(ExpressionParser.parse("mult(6.0, 6.0)").calculate())
    assertResult(1.0)(ExpressionParser.parse("div(6.0, 6.0)").calculate())
    assertResult(0.0)(ExpressionParser.parse("sub(6.0, 6.0)").calculate())
  }

  test("inner operations") {
    assertResult(12.0)(ExpressionParser.parse("add(add(3.0, 3.0), 6.0)").calculate())
    assertResult(36.0)(ExpressionParser.parse("mult(6.0, add(3.0, 3.0))").calculate())
    assertResult(12.0)(ExpressionParser.parse("add(add(3.0, 3.0),add(3.0, 3.0))").calculate())
    assertResult(4.0)(ExpressionParser.parse("add(1, add(1, add(1, 1)))").calculate())
    assertResult(4.0)(ExpressionParser.parse("add(add(add(1, 1), 1), 1)").calculate())
  }

  test("constant operations") {
    assertResult(1.0)(ExpressionParser.parse("div(pi, pi)").calculate())
  }
}
