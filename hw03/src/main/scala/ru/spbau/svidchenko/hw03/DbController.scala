package ru.spbau.svidchenko.hw03

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import ru.spbau.svidchenko.hw03.model.entity.{AnswerModel, PhraseModel}

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.DurationInt
import scala.util.Success

/**
  * Удобный интерфейс для общения с БД. (Но, на самом деле, он почти не используется)
  * @author ArgentumWalker
  */
class DbController(
                  answers: ActorRef
                  ) {
  def getPossibleAnswers(phraseModel: PhraseModel): mutable.Set[AnswerModel] = {
    var result: mutable.Set[AnswerModel] = mutable.HashSet.empty[AnswerModel]
    implicit val timeout: Timeout = Timeout(1.minute)
    implicit val ec = global
    Await.result(answers ? new model.List(phraseModel.wordValues), 1.minute).asInstanceOf[mutable.Set[AnswerModel]]
  }
}
