package ru.spbau.svidchenko.homework0000.calculator

import java.util

import ru.spbau.svidchenko.homework0000.calculator.expression.operations.{Addition, Subtraction}
import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Value}

/**
  * @author ArgentumWalker
  */
object ExpressionFactory {

  def value(value: Double): Value = new Value(value)

  def operation(name: String, arguments: util.ArrayList[Expression]): Expression = {
    name.toLowerCase match {
      case "sub" =>
        if (!arguments.size().equals(2)) {
          throw new exceptions.WrongNumberOfArgumentsException
        }
        new Subtraction(arguments.get(0), arguments.get(1))
      case "add" =>
        if (!arguments.size().equals(2)) {
          throw new exceptions.WrongNumberOfArgumentsException
        }
        new Addition(arguments.get(0), arguments.get(1))
      case _ => throw new UnsupportedOperationException
    }
  }
}
