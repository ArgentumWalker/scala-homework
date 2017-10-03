package ru.spbau.svidchenko.hw03.model.entity

import scala.collection.mutable

/**
  * Just set of represented words with simple matching-value function
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
  * Includes answer (string) and phrase model corresponding to original request phrase
  * @author ArgentumWalker
  */
class AnswerModel(val answer: String, val phraseModel: PhraseModel) extends Serializable
