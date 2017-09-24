package ru.spbau.svidchenko.hw03.model.builder

import java.io.InputStreamReader
import java.util
import java.util.Scanner

import akka.actor.ActorRef

import scala.util.Success
import akka.pattern.ask
import ru.spbau.svidchenko.hw03.model.entity.{AnswerModel, PhraseModel}
import ru.spbau.svidchenko.hw03.model.{AnswersActor, GetId, NewWord, VocabularyActor}
import org.tartarus.snowball.ext.RussianStemmer
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext.global
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.concurrent
import scala.concurrent.{Await, Future, Promise}
import scala.io.Source

/**
  * Инициализатор баз и фабрика для model.entity
  * @author ArgentumWalker
  */
class ModelBuilder(answers: ActorRef, vocabulary: ActorRef) {
  private val russian: RussianStemmer = new RussianStemmer

  def buildPhraseModel(phrase: String): PhraseModel = {
    val modelSet: mutable.Set[Long] = mutable.HashSet.empty
    val futuresToAwait: ArrayBuffer[Future[Any]] = new ArrayBuffer[Future[Any]]
    for (word <- phrase.split("[^а-яА-Я0-9-]+")) {
      russian.setCurrent(word.toLowerCase())
      russian.stem()
      val rootWord = russian.getCurrent

      implicit val timeout: Timeout = Timeout(3.minute)
      implicit val ec = global
      futuresToAwait += (vocabulary ? new GetId(rootWord))
    }
    for (future <- futuresToAwait) {
      modelSet += Await.result(future, 2.minute).asInstanceOf[Long]
    }
    new PhraseModel(modelSet)
  }

  def init(): Unit = {
    System.out.println("Инициализация словаря")
    initVocabularyActor(new Scanner(getClass.getClassLoader.getResourceAsStream("synonyms-vocabulary-alt-2.txt")))
    System.out.println("Инициализация пула ответов")
    initAnswersActor(new Scanner(getClass.getClassLoader.getResourceAsStream("dialog-pool.txt")))
    System.out.println("Запуск бота")
  }

  private def initVocabularyActor(
    synonymVocabulary: Scanner
  ) = {
    var wordCount: Long = 0
    val map: mutable.HashMap[String, VocabularyHelperHolder] = mutable.HashMap.empty
    val futuresToAwait: ArrayBuffer[Future[Any]] = new ArrayBuffer[Future[Any]]
    while (synonymVocabulary.hasNextLine) {
      val line = synonymVocabulary.nextLine()
      var synonyms: Array[String] = line.split(',')
      var parent: VocabularyHelperHolder = null
      val newHelpers: ArrayBuffer[VocabularyHelperHolder] = ArrayBuffer.empty

      for (word <- synonyms) {
        russian.setCurrent(word.toLowerCase())
        russian.stem()
        val rootWord: String = russian.getCurrent

        if (map.contains(rootWord)) {
          if (parent == null) {
            parent = map.getOrElse(rootWord, null)
          } else {
            map.getOrElse(rootWord, null).setParent(parent)
          }
        } else {
          newHelpers += new VocabularyHelperHolder(null, rootWord, wordCount)
          wordCount += 1
        }
      }

      if (parent == null) {
        parent = newHelpers(newHelpers.size - 1)
      }

      for (helper: VocabularyHelperHolder <- newHelpers) {
        helper.setParent(parent)
        implicit val timeout: Timeout = Timeout(1.minute)
        futuresToAwait += (vocabulary ? new NewWord(helper.getGroupId(), helper.word))
      }
    }
    for (future <- futuresToAwait) {
      Await.ready(future, 2.minute)
    }
  }

  private def initAnswersActor(
    dialogPoolVocabulary: Scanner
  ) = {
    var previous: String = ""
    while (dialogPoolVocabulary.hasNextLine) {
      val phrase = dialogPoolVocabulary.nextLine()
      if (!previous.isEmpty) {
        answers ! new AnswerModel(phrase, buildPhraseModel(previous))
      }
      previous = phrase
    }
  }

  private class VocabularyHelperHolder(
                                      private var parent: VocabularyHelperHolder,
                                      val word: String,
                                      private val groupId: Long
                                      ) {
    def setParent(from: VocabularyHelperHolder): Unit = {
      val newParent = from.getParent()
      if (newParent != this) {
        if (parent == null) {
          parent = newParent
        } else {
          getParent().setParent(newParent)
        }
      }
    }

    def getParent(): VocabularyHelperHolder = {
      if (parent == null) {
        return this
      }
      parent = parent.getParent()
      parent
    }

    def getGroupId(): Long = {
      getParent().groupId
    }
  }
}
