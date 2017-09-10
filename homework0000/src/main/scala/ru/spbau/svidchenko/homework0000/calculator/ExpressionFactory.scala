package ru.spbau.svidchenko.homework0000.calculator

import java.util

import ru.spbau.svidchenko.homework0000.calculator.expression.operations._
import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Value}

/**
  * @author ArgentumWalker
  */
object ExpressionFactory {

  def value(value: Double): Value = new Value(value)

  /**
    * Supported names:
    *   add - Addition. Arity = 2
    *   sub - Subtraction. Arity = 2
    *   mult - Multiplication. Arity = 2
    *   div - Division. Arity = 2
    *   pi - Pi constant. Arity = 0
    * @return operation with such name applied to arguments
    */
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
      case "div" =>
        if (!arguments.size().equals(2)) {
          throw new exceptions.WrongNumberOfArgumentsException
        }
        new Division(arguments.get(0), arguments.get(1))
      case "mult" =>
        if (!arguments.size().equals(2)) {
          throw new exceptions.WrongNumberOfArgumentsException
        }
        new Multiplication(arguments.get(0), arguments.get(1))
      case "pi" =>
        if (!arguments.size().equals(0)) {
          throw new exceptions.WrongNumberOfArgumentsException
        }
        new Pi()
      case _ => throw new UnsupportedOperationException
    }
  }
}
