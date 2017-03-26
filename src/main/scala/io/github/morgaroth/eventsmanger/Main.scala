package io.github.morgaroth.eventsmanger

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.scaladsl.{Flow, Sink}
import io.github.morgaroth.eventsmanger.evdev.InputEvent
import org.joda.time.{DateTime, Minutes}

import scala.concurrent.duration._
/*
https://github.com/progman32/evdev-java/blob/master/native/evdev-java.c
https://github.com/progman32/evdev-java/blob/master/src/com/dgis/input/evdev/EventDevice.java
http://stackoverflow.com/questions/3662368/dev-input-keyboard-format
 */

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    val grouper = new Grouper()
    val collector = new DataCollector("/dev/input/event4", "/dev/input/event5")
    val dbSaver = new PostgresSaver()

  }
}
