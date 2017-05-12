package io.github.morgaroth.eventsmanger

import akka.NotUsed
import akka.stream.scaladsl.Flow
import io.github.morgaroth.eventsmanger.evdev.InputEvent
import org.joda.time.{DateTime, Minutes, Seconds}

/**
  * Created by PRV on 23.03.2017.
  */
object Grouper {
  def apply(acceptableBreak: Minutes): Flow[InputEvent, Window, NotUsed] = {

    def logger[T](tag: String)(d: T): T = {
      println(s"$tag: $d")
      d
    }


    Flow[InputEvent]
      .filterNot(x => x.code == 0 && x.kind == 0)
      .statefulMapConcat(() => {
        var first: InputEvent = null
        var prev: InputEvent = null
        (ev: InputEvent) => {
          if (ev.isHalt) {
            List(if (prev.isTick) {
              Window.absent(first.ts, prev.ts)
            } else {
              Window.present(first.ts, prev.ts)
            })
          } else {
            if (first == null && !ev.isTick) {
              first = ev
            }
            val break = prev != null && (prev.isTick != ev.isTick && Seconds.secondsBetween(prev.ts, ev.ts).isGreaterThan(acceptableBreak.toStandardSeconds))
            val result = if (break) {
              val a = if (prev.isTick) {
                Window.absent(first.ts, prev.ts)
              } else {
                Window.present(first.ts, prev.ts)
              }
              first = ev
              List(a)
            } else Nil
            if (prev.isTick == ev.isTick) {
              prev = ev
            }
            result
          }
        }
      })
      .map(logger("\t\tready"))
  }
}
