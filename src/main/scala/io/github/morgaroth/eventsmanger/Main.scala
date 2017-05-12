package io.github.morgaroth.eventsmanger

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
import io.github.morgaroth.eventsmanger.evdev.{InputEvent, InputEventPublisher}
import org.joda.time.Minutes

import scala.concurrent.duration._

/*
https://github.com/progman32/evdev-java/blob/master/native/evdev-java.c
https://github.com/progman32/evdev-java/blob/master/src/com/dgis/input/evdev/EventDevice.java
http://stackoverflow.com/questions/3662368/dev-input-keyboard-format
 */

/*
docker run -d --restart always --name events-manager-pg -p 5440:5432 morgaroth/events-manager-pg:latest
 */
object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()
    import system.dispatcher

    val accBreak = Minutes.minutes(3)
    InputEventPublisher.populateWorkersFor(
      system,
      (accBreak.toStandardSeconds.getSeconds + 30).seconds,
      "/dev/input/event4", "/dev/input/event5"
    )


    Source
      .fromGraph(EventBusSource[InputEvent])
      .via(Grouper(accBreak))
      .via(PostgresSaver(ConfigFactory.load().getConfig("database")))
      .runWith(Sink.foreach(println(_)))
  }
}
