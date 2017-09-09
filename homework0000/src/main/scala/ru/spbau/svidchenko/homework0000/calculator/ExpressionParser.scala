package ru.spbau.svidchenko.homework0000.calculator

import java.util

import ru.spbau.svidchenko.homework0000.calculator.exceptions.SyntaxErrorException
import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Value}

/**
  * @author ArgentumWalker
  */
@throws(classOf[exceptions.EmptyExpressionException])
object ExpressionParser {
  def parse(expression: String): Expression = {
    val stringExpression: String = expression.replaceAll(" ","")
    if (stringExpression.isEmpty) {
      throw new exceptions.EmptyExpressionException
    }

    var argumentsStack: util.Stack[util.ArrayList[Expression]] = new util.Stack[util.ArrayList[Expression]]
    var namesStack: util.Stack[String] = new util.Stack[String]
    var startPos: Int = 0
    var state: Int = 0
    //0 - start parsing expression or parsing closing bracket sequence,
    //1  - parsing expression name
    //2 - parsing value decimal part, 3 - parsing value after point part

    for (pos <- 0 until stringExpression.length) {
      //Handle 0 state
      if (state.equals(0)) {
        stringExpression(pos) match {
          case ch if '0' to '9' contains ch => state = 2
          case '-' => state = 2
          case ')' =>
            if (argumentsStack.empty()) {
              throw new exceptions.SyntaxErrorException
            }
            val arguments: util.ArrayList[Expression] = argumentsStack.pop()
            val name: String = namesStack.pop()
            val closedExpression: Expression = ExpressionFactory.operation(name, arguments)
            if (!argumentsStack.empty()) {
              argumentsStack.peek().add(closedExpression)
            } else {
              if (!pos.equals(stringExpression.length - 1)) {
                throw new exceptions.SyntaxErrorException
              }
              return closedExpression
            }
          case ',' =>
            if (argumentsStack.empty()) {
              throw new exceptions.SyntaxErrorException
            }
          case ch if List('(', '.') contains ch => throw new exceptions.EmptyExpressionException
          case _ => state = 1
        }
        startPos = pos
      }
      state match {
        case 0 =>
          //Do nothing; Handler above
        case 1 =>
          stringExpression(pos) match {
            //Start argument matching
            case '(' =>
              state = 0
              namesStack.push(stringExpression.substring(startPos, pos))
              argumentsStack.push(new util.ArrayList[Expression]())
            //End argument matching of parent operation
            case ')' =>
              if (argumentsStack.empty()) {
                throw new exceptions.SyntaxErrorException
              }
              argumentsStack.peek().add(ExpressionFactory.operation(
                stringExpression.substring(startPos, pos),
                new util.ArrayList[Expression]()
              ))

              val arguments: util.ArrayList[Expression] = argumentsStack.pop()
              val name: String = namesStack.pop()
              val closedExpression: Expression = ExpressionFactory.operation(name, arguments)
              if (!argumentsStack.empty()) {
                argumentsStack.peek().add(closedExpression)
              } else {
                if (!pos.equals(stringExpression.length - 1)) {
                  throw new exceptions.SyntaxErrorException
                }
                return closedExpression
              }
            //Start next argument
            case ',' =>
              if (argumentsStack.empty()) {
                throw new exceptions.SyntaxErrorException
              }
              argumentsStack.peek().add(ExpressionFactory.operation(
                stringExpression.substring(startPos, pos),
                new util.ArrayList[Expression]()
              ))
              state = 0
            case '.' =>
              throw new exceptions.SyntaxErrorException
            case _ => //Do nothing
          }
        case st if List(2, 3) contains st =>
          stringExpression(pos) match {
            case '.' =>
              if (state.equals(3)) {
                throw new SyntaxErrorException
              }
              state = 3
            //Next argument of parent function
            case ',' =>
              if (namesStack.empty()) {
                throw new exceptions.SyntaxErrorException
              }
              state = 0
              val value : Value = ExpressionFactory.value(stringExpression.substring(startPos, pos).toDouble)
              argumentsStack.peek().add(value)
            //End argument matching of parent operation
            case ')' =>
              if (namesStack.empty()) {
                throw new exceptions.SyntaxErrorException
              }
              state = 0
              val value : Value = ExpressionFactory.value(stringExpression.substring(startPos, pos).toDouble)
              argumentsStack.peek().add(value)
              val arguments: util.ArrayList[Expression] = argumentsStack.pop()
              val name: String = namesStack.pop()
              val closedExpression: Expression = ExpressionFactory.operation(name, arguments)
              if (!argumentsStack.empty()) {
                argumentsStack.peek().add(closedExpression)
              } else {
                if (!pos.equals(stringExpression.length - 1)) {
                  throw new exceptions.SyntaxErrorException
                }
                return closedExpression
              }
            case _ => //Do nothing
          }
      }
    }
    //If expression still not constructed
    if (!namesStack.empty() || state.equals(0)) {
      throw new exceptions.SyntaxErrorException
    }
    if (state.equals(1)) {
      ExpressionFactory.operation(
        stringExpression.substring(startPos),
        new util.ArrayList[Expression]()
      )
    } else {
      ExpressionFactory.value(stringExpression.substring(startPos).toDouble)
    }
  }
}
