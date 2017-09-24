package ru.spbau.svidchenko.homework0000.calculator

import java.util

import ru.spbau.svidchenko.homework0000.calculator.exceptions.SyntaxErrorException
import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Value}

/**
  * Parser for expressions
  * @author ArgentumWalker
  */
object ExpressionParser {
  /**
    * Supports double values and operations (see ExpressionFactory for supported operation names)
    * Values format: [-][0-9]+\(.[0-9]+\)?
    * Operation with arguments format: name\(argument(, argument)+\)
    * Operation without arguments format: [name]\(()\)?
    * @return parsed expression
    */
  def parse(expression: String): Expression = {
    val stringExpression: String = expression.replaceAll(" ","")
    if (stringExpression.isEmpty) {
      throw new exceptions.EmptyExpressionException
    }

    var parseState : ParseState = new ParseState
    //0 - start parsing expression or parsing closing bracket sequence,
    //1  - parsing expression name, 2 - parsing closing bracket sequence,
    //3 - parsing value decimal part, 4 - parsing value after point part

    for (pos <- 0 until stringExpression.length) {
      parseState.pos = pos
      parseState.state match {
        case 0 => startOfExpressionState(parseState, stringExpression)
        case 1 => expressionNameState(parseState, stringExpression)
        case 2 => closingBracketSequenceState(parseState, stringExpression)
        case x if List(3, 4) contains x => valueState(parseState, stringExpression)
      }
    }
    //If expression still not constructed
    if (!parseState.namesStack.empty() || parseState.state.equals(0)) {
      throw new exceptions.SyntaxErrorException
    }
    if (parseState.finished) {
      return parseState.finalExpression
    }
    if (parseState.state.equals(1)) {
      ExpressionFactory.operation(
        stringExpression.substring(parseState.startPos),
        new util.ArrayList[Expression]()
      )
    } else {
      ExpressionFactory.value(stringExpression.substring(parseState.startPos).toDouble)
    }
  }

  private def startOfExpressionState(parseState: ParseState, expression: String): Unit = {
    parseState.startPos = parseState.pos
    expression(parseState.pos) match {
      case ch if '0' to '9' contains ch =>
        parseState.state = 3
        valueState(parseState, expression)
      case '-' =>
        parseState.state = 3
        valueState(parseState, expression)
      case ch if List('(', '.', ')', ',') contains ch => throw new exceptions.EmptyExpressionException
      case _ =>
        parseState.state = 1
        expressionNameState(parseState, expression)
    }
  }

  private def closingBracketSequenceState(parseState: ParseState, expression: String): Unit = {
    expression(parseState.pos) match  {
      case ')' =>
        buildTopExpression(parseState, expression)
      case ',' =>
        parseState.state = 0
      case _ =>
        throw new exceptions.SyntaxErrorException
    }
  }

  private def expressionNameState(parseState: ParseState, expression: String): Unit = {
    expression(parseState.pos) match {
      //Start argument matching
      case '(' =>
        parseState.state = 0
        parseState.namesStack.push(expression.substring(parseState.startPos, parseState.pos))
        parseState.argumentsStack.push(new util.ArrayList[Expression]())
      //End argument matching of parent operation
      case ')' =>
        if (parseState.argumentsStack.empty()) {
          throw new exceptions.SyntaxErrorException
        }
        parseState.argumentsStack.peek().add(ExpressionFactory.operation(
          expression.substring(parseState.startPos, parseState.pos),
          new util.ArrayList[Expression]()
        ))
        buildTopExpression(parseState, expression)
      //Start next argument
      case ',' =>
        if (parseState.argumentsStack.empty()) {
          throw new exceptions.SyntaxErrorException
        }
        parseState.argumentsStack.peek().add(ExpressionFactory.operation(
          expression.substring(parseState.startPos, parseState.pos),
          new util.ArrayList[Expression]()
        ))
        parseState.state = 0
      case '.' =>
        throw new exceptions.SyntaxErrorException
      case _ => //Do nothing
    }
  }

  private def valueState(parseState: ParseState, expression: String): Unit = {
    expression(parseState.pos) match {
      case '.' =>
        if (parseState.state.equals(4)) {
          throw new SyntaxErrorException
        }
        parseState.state = 4
      //Next argument of parent function
      case ',' =>
        if (parseState.namesStack.empty()) {
          throw new exceptions.SyntaxErrorException
        }
        parseState.state = 0
        val value : Value = ExpressionFactory.value(expression.substring(parseState.startPos, parseState.pos).toDouble)
        parseState.argumentsStack.peek().add(value)
      //End argument matching of parent operation
      case ')' =>
        if (parseState.namesStack.empty()) {
          throw new exceptions.SyntaxErrorException
        }
        parseState.state = 2
        parseState.argumentsStack.peek().add(
          ExpressionFactory.value(expression.substring(parseState.startPos, parseState.pos).toDouble)
        )
        buildTopExpression(parseState, expression)
      case _ => //Do nothing
    }
  }

  private def buildTopExpression(parseState: ParseState, expression: String): Unit = {
    if (parseState.argumentsStack.empty()) {
      throw new exceptions.SyntaxErrorException
    }
    val arguments: util.ArrayList[Expression] = parseState.argumentsStack.pop()
    val name: String = parseState.namesStack.pop()
    val closedExpression: Expression = ExpressionFactory.operation(name, arguments)
    if (!parseState.argumentsStack.empty()) {
      parseState.argumentsStack.peek().add(closedExpression)
    } else {
      if (!parseState.pos.equals(expression.length - 1)) {
        throw new exceptions.SyntaxErrorException
      }
      parseState.finished = true
      parseState.finalExpression = closedExpression
    }
  }

  private class ParseState {
    var pos: Int = 0
    var state: Int = 0
    var startPos: Int = 0
    var argumentsStack: util.Stack[util.ArrayList[Expression]] = new util.Stack[util.ArrayList[Expression]]
    var namesStack: util.Stack[String] = new util.Stack[String]
    var finished: Boolean = false
    var finalExpression: Expression = _
  }
}
