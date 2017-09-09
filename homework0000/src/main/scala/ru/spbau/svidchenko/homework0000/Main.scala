package ru.spbau.svidchenko.homework0000

import ru.spbau.svidchenko.homework0000.calculator.ExpressionParser

/**
  * @author ArgentumWalker
  */
object Main {

  def main(args: Array[String]): Unit = {
    var expression : String = scala.io.StdIn.readLine()
    println(ExpressionParser.parse(expression).calculate())
  }
}
