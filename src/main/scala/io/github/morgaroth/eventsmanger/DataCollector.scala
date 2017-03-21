package io.github.morgaroth.eventsmanger

import java.io._
import java.nio.ByteBuffer

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}
import io.github.morgaroth.eventsmanger.evdev.{EvDevSource, InputEvent}

/**
  * Created by PRV on 21.03.2017.
  */
class DataCollector(sources: String*)(implicit actorSystem: ActorSystem, mat: Materializer) {

  def runWith(sink: Sink[InputEvent, NotUsed]) = {
    val inputs = sources.filter { path =>
      val file = new File(path)
      println(s"Could be $path readable? ${file.canRead}")
      file.canRead
    }.map(x => new File(x))

    if (inputs.nonEmpty) {
      val a = Source.actorRef[(ByteBuffer, String)](Int.MaxValue, OverflowStrategy.dropNew)
      val f = Flow[(ByteBuffer, String)].map { case (buf, source) => InputEvent.parse(buf.asShortBuffer(), source) }.to(sink).runWith(a)
      inputs.foreach { input =>
        EvDevSource(input).runForeach(f ! _)
      }
      Some(f)
    } else None
  }
}
