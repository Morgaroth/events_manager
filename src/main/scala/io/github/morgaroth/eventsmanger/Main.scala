package io.github.morgaroth.eventsmanger

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink}
import io.github.morgaroth.eventsmanger.evdev.InputEvent
import org.joda.time.{DateTime, Minutes}

import scala.concurrent.duration._

/*
https://github.com/progman32/evdev-java/blob/master/native/evdev-java.c
https://github.com/progman32/evdev-java/blob/master/src/com/dgis/input/evdev/EventDevice.java
http://stackoverflow.com/questions/3662368/dev-input-keyboard-format
 */


case class Window(start: DateTime, end: DateTime, present: Boolean, accepted: Boolean = false, tags: List[String] = List("untagged"))

object Window {
  def present(start: DateTime, end: DateTime) = Window(start, end, present = true)

  def absent(start: DateTime, end: DateTime) = Window(start, end, present = false)
}

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()


    val acceptableBreak = Minutes.minutes(5)

    def logger[T](tag: String)(d: T): T = {
      println(s"$tag: $d")
      d
    }

    val reducerFlow: Sink[InputEvent, NotUsed] = Flow[InputEvent]
      .groupedWithin(100, 1.second)
      .map { events =>
        val sorted = events.toList.map(x => x.time_sec -> x).sortBy(_._1)
        sorted(sorted.length / 2)._2
      }
      .groupedWithin(Int.MaxValue, 1.second)
      .map { events =>
        val sorted = events.toList.map(x => x.time_sec -> x).sortBy(_._1)
        sorted(sorted.length / 2)._2
      }
      .statefulMapConcat { () =>
        var lastSec = -1l
        (ev: InputEvent) => {
          val break = ev.time_sec > lastSec && lastSec > 0
          if (break) lastSec = ev.time_sec
          List((break, ev))
        }
      }
      .splitWhen(_._1)
      .groupedWithin(Int.MaxValue, 2.minutes)
      .map(_.head._2.ts.withMillisOfSecond(0).withSecondOfMinute(0))
      .concatSubstreams
      .map(logger("\tby-minute"))
      .statefulMapConcat { () =>
        var prev: DateTime = null
        (ev: DateTime) => {
          val break = prev != null && Minutes.minutesBetween(prev, ev).isGreaterThan(acceptableBreak)
          prev = ev
          List((break, ev))
        }
      }.splitWhen(_._1).map(_._2).fold((null: DateTime, null: DateTime)) {
      case ((null, _), start) => (start, null)
      case (x, next) => (x._1, next)
    }.filter(x => x._1 != null && x._2 != null)
      .concatSubstreams
      .map(logger("\t\tready"))
      .sliding(2).map(x => (x.head, x.last))
      .mapConcat {
        case ((start1, end1), (start2, _)) => List(Window.present(start1, end1), Window.absent(end1, start2))
      }.to(Sink.foreach(println(_)))


    new DataCollector("/dev/input/event4", "/dev/input/event5").runWith(reducerFlow)
  }
}
