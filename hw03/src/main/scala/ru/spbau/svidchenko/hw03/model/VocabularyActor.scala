package ru.spbau.svidchenko.hw03.model

import akka.persistence.PersistentActor

import scala.collection.mutable

/**
  * Created by ArgentumWalker on 23.09.2017.
  */
class VocabularyActor extends PersistentActor  {
  val vocabulary: mutable.HashMap[String, Long] = new mutable.HashMap[String, Long]

  def receiveAdd(addRequest: NewWord): Unit = {
    vocabulary += ((addRequest.value, addRequest.id))
  }

  override def receiveRecover: Receive = {
    case word: NewWord => receiveAdd(word)
  }

  override def receiveCommand: Receive = {
    case word: NewWord =>
      persist(word)(receiveAdd)
    case get: GetId =>
      sender ! vocabulary.getOrElse(get.value, -1L)
  }

  override def persistenceId = "vocabulary-database"
}

class NewWord(val id: Long, val value: String) extends Serializable
class GetId(val value: String)
