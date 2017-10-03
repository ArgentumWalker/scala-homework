package ru.spbau.svidchenko.homework0000.calculator.expression.operations

import ru.spbau.svidchenko.homework0000.calculator.expression.Expression

/**
  * @author ArgentumWalker
  */
class Multiplication(a : Expression, b : Expression) extends Expression {
  override def calculate(): Double = a.calculate() * b.calculate()
}
