package io.github.morgaroth.eventsmanger

import java.io._
import java.nio.ByteBuffer

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}
import io.github.morgaroth.eventsmanger.evdev.{EvDevSource, InputEvent}

/*
https://github.com/progman32/evdev-java/blob/master/native/evdev-java.c
https://github.com/progman32/evdev-java/blob/master/src/com/dgis/input/evdev/EventDevice.java
http://stackoverflow.com/questions/3662368/dev-input-keyboard-format
 */

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    val path1 = "/dev/input/event4"
    // mouse
    val path2 = "/dev/input/event5" // keyboard

    println(s"Can read? ${new File(path1).canRead}")
    println(s"Can read? ${new File(path2).canRead}")

    val a = Source.actorRef[(ByteBuffer, String)](Int.MaxValue, OverflowStrategy.dropNew)
    val f = Flow[(ByteBuffer, String)].map { case (buf, source) => InputEvent.parse(buf.asShortBuffer(), source) }.to(Sink.foreach(println(_))).runWith(a)
    EvDevSource(path1).runForeach(f ! _)
    EvDevSource(path2).runForeach(f ! _)
  }

}
