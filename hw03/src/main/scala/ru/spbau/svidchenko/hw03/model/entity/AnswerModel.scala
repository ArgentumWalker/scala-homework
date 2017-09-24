package ru.spbau.svidchenko.hw03.model.entity

import scala.collection.mutable

/**
  * Created by ArgentumWalker on 23.09.2017.
  */
class PhraseModel(val wordValues : mutable.Set[Long]) extends Serializable {
  def worth(phraseModel: PhraseModel): Long = {
    2 * phraseModel.wordValues.intersect(wordValues).size -
      (wordValues -- phraseModel.wordValues).size -
      (phraseModel.wordValues -- wordValues).size
  }
}

class AnswerModel(val answer: String, val phraseModel: PhraseModel) extends Serializable
