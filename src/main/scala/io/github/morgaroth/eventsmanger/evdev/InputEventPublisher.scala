package io.github.morgaroth.eventsmanger.evdev

import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.nio.{ByteBuffer, ByteOrder}

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.concurrent.duration._

object InputEventPublisher {
  def populateWorkerFor(file: File, as: ActorSystem) = {
    as.actorOf(Props(new InputEventPublisher(file)), file.getCanonicalPath.replace("/", "_"))
  }

  def populateWorkersFor(as: ActorSystem, tickInterval: FiniteDuration, sources: String*) = {
    val availableFiles: Seq[File] = sources.map { path =>
      val file = new File(path)
      println(s"Could be $path readable? ${file.canRead}")
      (file, file.canRead)
    }.collect { case (f, true) => f }

    availableFiles.foreach(f => populateWorkerFor(f, as))
    as.actorOf(Props(new TickEventPublisher(tickInterval)), "tick-publisher")
  }
}

class InputEventPublisher(file: File) extends Actor with ActorLogging {

  val channel = FileChannel.open(file.toPath, StandardOpenOption.READ)
  var stop = false

  def getSth: Option[InputEvent] = {
    try {
      val buf = ByteBuffer.allocateDirect(InputEvent.STRUCT_SIZE_BYTES)
      buf.order(ByteOrder.LITTLE_ENDIAN)
      channel.read(buf)
      buf.flip()
      Some(InputEvent.parse(buf.asShortBuffer(), file.getPath))
    } catch {
      case t: Throwable =>
        log.warning("exception during reading event input {} of class {}", t.getMessage, t.getClass.getCanonicalName)
        None
    }
  }

  self ! 1

  override def receive = {
    case 1 =>
      getSth.foreach(context.system.eventStream.publish(_))
      self ! 1
  }

  override def postStop() = {
    channel.close()
    super.postStop()
  }
}

case object Tick

class TickEventPublisher(tickInterval: FiniteDuration) extends Actor {

  context.system.scheduler.schedule(0.seconds, tickInterval, self, Tick)

  override def receive: Receive = {
    case Tick =>
      context.system.eventStream.publish(InputEvent.tick)
  }
}
