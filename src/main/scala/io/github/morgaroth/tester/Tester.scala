package io.github.morgaroth.tester

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.model.Multipart.FormData
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.stream.ActorMaterializer
import akka.util.ByteString

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Success

/**
  * Created by PRV on 27.03.2017.
  */
class Tester extends App {


  val questionsN = 10
  implicit val as = ActorSystem("fds")
  implicit val mat = ActorMaterializer()

  import as.dispatcher

  val blank = List.fill(questionsN)('A')

  def gen(pos: Int = 1): List[List[((Char, Int), String)]] = {
    if (pos <= questionsN) List(
      ('A', pos) -> blank.mkString,
      ('B', pos) -> (blank.take(pos - 1) ::: ('B' :: blank.takeRight(questionsN - pos))).mkString,
      ('C', pos) -> (blank.take(pos - 1) ::: ('C' :: blank.takeRight(questionsN - pos))).mkString,
      ('D', pos) -> (blank.take(pos - 1) ::: ('D' :: blank.takeRight(questionsN - pos))).mkString
    ) :: gen(pos + 1)
    else Nil
  }

  println(gen().mkString("\n"))

  val possibilities = gen()


  def check(option: String): Future[Int] = {
    val data = option.zipWithIndex.map {
      x => s"question-${x._2 + 1}-answers" -> x._2
    }
    val forms = data.map(x => x._1 -> HttpEntity.Strict(ContentTypes.`text/plain(UTF-8)`, ByteString(x._2))).toMap
    akka.pattern.after(2.seconds, as.scheduler)(Future.successful {
      println(option)
    }).flatMap(_ => Http().singleRequest(Post("http://www.antosz.me/quiz/Czlowiek/grade.php", FormData(forms)))
      .flatMap(_.entity.toStrict(1.minute))
      .map(_.data.decodeString("utf-8"))
      .map { x => println(x); x }
      .map(x => x.drop(x.indexOf("results") + 9))
      .map { x => println(x); x }
      .map(x => x.take(x.indexOf("/") - 1))
      .map(_.toInt))
  }

  possibilities.foldLeft(Future.successful(List.empty[(Char, Int)])) { case (acc, options) =>
    acc.flatMap { all =>
      Future.sequence(options.map {
        case (x, opt) => check(opt).map(x -> _).map {
          x =>
            println(x)
            x
        }
      }).map(_.sortBy(_._2))
        .map(y => y.groupBy(_._2).toList.sortBy(_._1).flatMap(_._2)).map(r => all ::: r.map(_._1))
    }
  }.map(_.groupBy(_._2).mapValues(_.map(_._1).sorted)).onComplete {
    case Success(result) =>
      println(result.mkString("\n"))
      as.terminate()
    case a =>
      println(s"ERROR $a")
      as.terminate()
  }
}
