package ru.spbau.svidchenko.hw03.model.entity

import scala.collection.mutable

/**
  * Модель фразы. В данной реализации - просто набор слов с возможностью посчитать совподение с другой моделью.
  * @author ArgentumWalker
  */
class PhraseModel(val wordValues : mutable.Set[Long]) extends Serializable {
  def worth(phraseModel: PhraseModel): Long = {
    2 * phraseModel.wordValues.intersect(wordValues).size -
      (wordValues -- phraseModel.wordValues).size -
      (phraseModel.wordValues -- wordValues).size
  }
}

/**
  * Модель ответа. В данной реализации - ответ и модель фразы-запроса
  * @author ArgentumWalker
  */
class AnswerModel(val answer: String, val phraseModel: PhraseModel) extends Serializable
