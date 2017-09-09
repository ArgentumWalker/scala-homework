package ru.spbau.svidchenko.homework0000.calculator

import java.util

import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Operation, Value}

/**
  * @author ArgentumWalker
  */
object ExpressionFactory {

  def value(value: Double): Value = new Value(value)

  def operation(name: String, arguments: util.ArrayList[Expression]): Operation = {
    name.toLowerCase match {
      case _ => throw new UnsupportedOperationException
    }
  }
}
