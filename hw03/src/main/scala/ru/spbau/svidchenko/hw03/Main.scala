package ru.spbau.svidchenko.hw03

import akka.actor.{ActorSystem, Props}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension
import ru.spbau.svidchenko.hw03.model.builder.ModelBuilder
import ru.spbau.svidchenko.hw03.model.{AnswersActor, VocabularyActor}

/**
  * Простой бот-болталка, который пытается поддерживать диалог
  * @author ArgentumWalker
  */
object Main extends App{
  //databases
  val system = ActorSystem()
  val answers = system.actorOf(Props(classOf[AnswersActor]))
  val vocabulary = system.actorOf(Props(classOf[VocabularyActor]))
  val modelsBuilder = new ModelBuilder(answers, vocabulary)
  modelsBuilder.init()

  //Logic
  val dbController = new DbController(answers)
  val answerLogic = new AnswerLogic(dbController, modelsBuilder)

  //telegram
  val token = "400975053:AAH2bAGyXFYVugPUgH8YqooTMAJ4ggrSCvY"
  private val bot = new Telegram(token, answerLogic);

  val scheduler = QuartzSchedulerExtension(system)
  val actor = system.actorOf(Props(classOf[AskActor], bot))

  scheduler.createSchedule("every minute", None, "	0/1 * * 1/1 * ? *")
  scheduler.schedule("every minute", actor, "Ask")

  bot.run()
}
