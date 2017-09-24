package ru.spbau.svidchenko.homework0000.calculator

import java.util

import org.scalatest.FunSuite
import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Value}

/**
  * @author ArgentumWalker
  */
class ExpressionFactoryTest extends FunSuite {
  test("assert operation classes") {
    val values: util.ArrayList[Expression] = new util.ArrayList[Expression]()
    values.add(new Value(1))
    values.add(new Value(2))
    val empty: util.ArrayList[Expression] = new util.ArrayList[Expression]()

    assert(ExpressionFactory.operation("add", values).isInstanceOf[expression.operations.Addition])
    assert(ExpressionFactory.operation("mult", values).isInstanceOf[expression.operations.Multiplication])
    assert(ExpressionFactory.operation("sub", values).isInstanceOf[expression.operations.Subtraction])
    assert(ExpressionFactory.operation("div", values).isInstanceOf[expression.operations.Division])
    assert(ExpressionFactory.operation("pi", empty).isInstanceOf[expression.operations.Pi])
  }
}
