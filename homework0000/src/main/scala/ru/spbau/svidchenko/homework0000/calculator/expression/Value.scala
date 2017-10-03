package ru.spbau.svidchenko.homework0000.calculator.expression

/**
  * @author ArgentumWalker
  */
class Value(number: Double) extends Expression{
  override def calculate(): Double = number
}
