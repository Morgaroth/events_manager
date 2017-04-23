package io.github.morgaroth.eventsmanger

import akka.NotUsed
import akka.stream.scaladsl.Flow
import io.github.morgaroth.eventsmanger.evdev.InputEvent
import org.joda.time.{DateTime, Minutes}

import scala.concurrent.duration._

/**
  * Created by PRV on 23.03.2017.
  */
object Grouper {
  def apply(): Flow[InputEvent, Window, NotUsed] = {

    val acceptableBreak = Minutes.minutes(5)

    def logger[T](tag: String)(d: T): T = {
      println(s"$tag: $d")
      d
    }

    Flow[InputEvent].groupedWithin(100, 1.second)
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
      }
  }
}
