package io.github.morgaroth.eventsmanger

import akka.NotUsed
import akka.stream.scaladsl.Flow
import io.github.morgaroth.eventsmanger.evdev.InputEvent
import org.joda.time.{DateTime, Minutes}

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

    def grouper[T, U](diffWhen: (T, T) => Boolean, reduce: List[T] => U): Flow[T, U, NotUsed] = Flow[T]
      .statefulMapConcat(() => {
        var lastElem: Option[T] = None
        (ev: T) => {
          val break = lastElem.exists(diffWhen(_, ev))
          lastElem = Some(ev)
          List((break, ev))
        }
      })
      .splitWhen(_._1)
      .map(_._2)
      .fold(List.empty[T]) { case (acc, ev) => acc :+ ev }
      .map(x => reduce(x))
      .concatSubstreams


    Flow[InputEvent]
      .via(grouper[InputEvent, InputEvent](_.time_sec < _.time_sec, x => {
        val size = x.size
        x.sortBy(_.time_sec).apply(size / 2)
      }))
      .map(logger("\tby-second"))
      .via(grouper[InputEvent, DateTime](_.ts.minuteOfHour != _.ts.minuteOfHour, x => {
        val size = x.size
        x.sortBy(_.time_sec).apply(size / 2).ts.withSecondOfMinute(0).withMillisOfSecond(0)
      }))
      .map(logger("\tby-minute"))
      .statefulMapConcat(() => {
        var prev: DateTime = null
        (ev: DateTime) => {
          val break = prev != null && Minutes.minutesBetween(prev, ev).isGreaterThan(acceptableBreak)
          prev = ev
          List((break, ev))
        }
      }).splitWhen(_._1).map(_._2).fold((null: DateTime, null: DateTime)) {
      case ((null, _), start) => (start, null)
      case (x, next) => (x._1, next)
    }
      .filter(x => x._1 != null && x._2 != null)
      .concatSubstreams
      .map(logger("\t\tready"))
      .sliding(2).map(x => (x.head, x.last))
      .mapConcat {
        case ((start1, end1), (start2, _)) => List(Window.present(start1, end1), Window.absent(end1, start2))
      }
  }
}
