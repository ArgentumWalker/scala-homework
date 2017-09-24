package ru.spbau.svidchenko.hw03.model

import akka.persistence.PersistentActor
import ru.spbau.svidchenko.hw03.model.entity.{AnswerModel, PhraseModel}

import scala.collection.mutable

/**
  * Created by ArgentumWalker on 23.09.2017.
  */
class AnswersActor extends PersistentActor {
  val answers: mutable.MultiMap[Long, AnswerModel] =
    new mutable.HashMap[Long, mutable.Set[AnswerModel]]
    with mutable.MultiMap[Long, AnswerModel]

  def receiveAdd(addRequest: AnswerModel): Unit = {
    for (word <- addRequest.phraseModel.wordValues) {
      if (answers.contains(word)) {
        answers += ((word, answers(word) + addRequest))
      } else {
        answers += ((word, mutable.HashSet(addRequest)))
      }
    }
  }

  override def receiveRecover: Receive = {
    case ans: AnswerModel => receiveAdd(ans)
  }

  override def receiveCommand: Receive = {
    case ans: AnswerModel => persist(ans)(receiveAdd)
    case list: List =>
      val result = mutable.HashSet.empty[AnswerModel]
      for (num <- list.wordValues) {
        if (answers.contains(num)) {
          result ++= answers(num)
        }
      }
      sender ! result
  }

  override def persistenceId = "answer-database"
}

//queries
class List(val wordValues : mutable.Set[Long])