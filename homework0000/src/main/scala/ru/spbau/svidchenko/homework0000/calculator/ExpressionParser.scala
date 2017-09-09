package ru.spbau.svidchenko.homework0000.calculator

import java.util

import ru.spbau.svidchenko.homework0000.calculator.expression.{Expression, Operation, Value}

/**
  * @author ArgentumWalker
  */
@throws(classOf[exceptions.EmptyExpressionException])
object ExpressionParser {
  def parse(stringExpression: String): Expression = {
    //ToDo: cut spaces;
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
          case ch if List('(', '.', ',') contains ch => throw new exceptions.EmptyExpressionException
          case _ => state = 1
        }
        startPos = pos
      }
      state match {
        case 1 =>
          stringExpression(pos) match {
            case '(' =>
              state = 0
              namesStack.push(stringExpression.substring(startPos, pos - 1))
              argumentsStack.push(new util.ArrayList[Expression]())
            case '.' =>
              throw new exceptions.SyntaxErrorException
            case ')' =>
              if (argumentsStack.empty()) {
                throw new exceptions.SyntaxErrorException
              }
              val currentOperation: Operation = ExpressionFactory.operation(
                stringExpression.substring(startPos, pos),
                new util.ArrayList[Expression]()
              )
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
              argumentsStack.peek().add(ExpressionFactory.operation(
                stringExpression.substring(startPos, pos),
                new util.ArrayList[Expression]()
              ))
              state = 0
          }
        case 2 =>
          stringExpression(pos) match {
            case '.' => state = 3
            case ',' =>
              if (namesStack.empty()) {
                throw new exceptions.SyntaxErrorException
              }
              state = 0
              val value : Value = ExpressionFactory.value(stringExpression.substring(startPos, pos - 1).toDouble)
              argumentsStack.peek().add(value)
            case ')' =>

            case ch if '0' to '9' contains ch => //Do nothing
            case '-' => //Do nothing
            case _ => throw new exceptions.SyntaxErrorException
          }
      }
    }
  }
}
