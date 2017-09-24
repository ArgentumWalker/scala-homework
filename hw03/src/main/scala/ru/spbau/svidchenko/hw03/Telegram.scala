package ru.spbau.svidchenko.hw03

import akka.actor.Actor
import info.mukel.telegrambot4s.api.declarative.Commands
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}

import scala.collection.mutable

/**
  * Телеграм бот
  * @author ArgentumWalker
  */

class AskActor(bot: Telegram) extends Actor {
  override def receive = {
    case _ => bot.askUsers()
  }
}

class Telegram (
               val token : String,
               val answerLogic : AnswerLogic
               ) extends TelegramBot with Polling with Commands  {

  val map: mutable.HashMap[Long, String] = mutable.HashMap.empty

  def askUsers(): Unit = {}

  onMessage {
    implicit messsage =>
      messsage.text.foreach {
        text => {
          reply(answerLogic(text))
        }
      }
  }

}
