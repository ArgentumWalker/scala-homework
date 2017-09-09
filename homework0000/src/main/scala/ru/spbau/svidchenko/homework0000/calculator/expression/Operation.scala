package ru.spbau.svidchenko.homework0000.calculator.expression

/**
  * @author ArgentumWalker
  */
trait Operation extends Expression{
  def arity(): Int
}
