package ru.spbau.svidchenko.hw03

import java.util
import java.util.Random

import ru.spbau.svidchenko.hw03.model.builder.ModelBuilder
import ru.spbau.svidchenko.hw03.model.entity.AnswerModel

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ArgentumWalker on 23.09.2017.
  */
class AnswerLogic(
                 val dbController: DbController,
                 val modelBuilder: ModelBuilder
                 ) {
  private val random: Random = new Random()

  def apply(message: String): String = {
    val phraseModel = modelBuilder.buildPhraseModel(message)
    val possibleAnswers = dbController.getPossibleAnswers(phraseModel)
    if (possibleAnswers.isEmpty) {
      return "Я не понимаю. :("
    }
    var bestWorth: Long = -1000000
    val bestMatches: ArrayBuffer[AnswerModel] = new ArrayBuffer[AnswerModel]()
    for (answer: AnswerModel <- possibleAnswers) {
      var currentWorth = answer.phraseModel.worth(phraseModel)
      if (bestWorth < currentWorth) {
        bestMatches.clear()
        bestWorth = currentWorth
        System.out.println("----------new best worth")
      }
      if (bestWorth == currentWorth) {
        bestMatches += answer
        System.out.println(answer.answer)
      }
    }
    System.out.println("End")
    bestMatches(random.nextInt(bestMatches.size)).answer
  }
}
