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

    val acceptableBreak = Minutes.minutes(0)

    def logger[T](tag: String)(d: T): T = {
      println(s"$tag: $d")
      d
    }

    val f: Flow[InputEvent, (DateTime, DateTime), NotUsed] = Flow[InputEvent]
      .filterNot(x => x.code == 0 && x.kind == 0)
      .map(logger("ev"))
      .map(_.ts)
      .statefulMapConcat(() => {
        var first: DateTime = null
        var prev: DateTime = null
        (ev: DateTime) => {
          if (first == null) {
            first = ev
          }
          val break = prev != null && Minutes.minutesBetween(prev, ev).isGreaterThan(acceptableBreak)
          val result = if (break) {
            val a = List(first -> prev)
            first = ev
            a
          } else Nil
          prev = ev
          result
        }
      })
    f.map(logger("\t\tready"))
      .sliding(2).map(x => (x.head, x.last))
      .mapConcat {
        case ((start1, end1), (start2, _)) => List(Window.present(start1, end1), Window.absent(end1, start2))
      }
  }
}
