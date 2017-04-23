package io.github.morgaroth.eventsmanger.evdev

import java.io.File
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption
import java.nio.{ByteBuffer, ByteOrder}

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern.ask
import akka.stream.scaladsl.Source
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.util.Timeout
import io.github.morgaroth.eventsmanger.evdev.Types.Data

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object EvDevSource {
  def apply(file: File): Source[(ByteBuffer, String), NotUsed] = Source.unfoldResource[(ByteBuffer, String), FileChannel](
    () => FileChannel.open(file.toPath, StandardOpenOption.READ), { chan =>
      val buf = ByteBuffer.allocateDirect(InputEvent.STRUCT_SIZE_BYTES)
      buf.order(ByteOrder.LITTLE_ENDIAN)
      chan.read(buf)
      buf.flip()
      Some((buf, file.getPath))
    }, _.close()
  )
}

object Types {
  type Data = (ByteBuffer, String)
}

class Worker(file: File) extends Actor with ActorLogging {

  val channel = FileChannel.open(file.toPath, StandardOpenOption.READ)
  var stop = false

  def getSth: Data = {
    val buf = ByteBuffer.allocateDirect(InputEvent.STRUCT_SIZE_BYTES)
    buf.order(ByteOrder.LITTLE_ENDIAN)
    channel.read(buf)
    buf.flip()
    (buf, file.getPath)
  }

  override def receive = {
    case 1 if !stop =>
      val data = getSth
      context.parent ! data
      self ! 1
  }

  override def postStop() = {
    channel.close()
    super.postStop()
  }
}

object Worker2 {
  def populateWorkerFor(file: File, as: ActorSystem) = {
    as.actorOf(Props(new InputEventPublisher(file)), file.getCanonicalPath)
  }

  def populateWorkersFor(as: ActorSystem, sources: String*) = {
    val availableFiles: Seq[File] = sources.map { path =>
      val file = new File(path)
      println(s"Could be $path readable? ${file.canRead}")
      (file, file.canRead)
    }.collect { case (f, true) => f }

    availableFiles.foreach(f => populateWorkerFor(f, as))

  }
}

class Worker2(file: File) extends Actor with ActorLogging {

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

class Collector(sources: List[String]) extends Actor with ActorLogging {
  val availableFiles: List[File] = sources.map { path =>
    val file = new File(path)
    println(s"Could be $path readable? ${file.canRead}")
    (file, file.canRead)
  }.collect { case (f, true) => f }

  availableFiles.foreach(f => context.actorOf(Props(new Worker(f))))

  val queue = mutable.Queue.empty[Data]

  override def receive = {
    case data: Data =>
      queue.enqueue(data)
    case i: Int =>
      sender() ! List.fill(i)(Try(queue.dequeue()).toOption).collect { case Some(a) => a }
  }
}


class EventsSource(sources: List[String], system: ActorSystem) extends GraphStage[SourceShape[Data]] {
  // Define the (sole) output port of this stage
  val out: Outlet[Data] = Outlet("EventsSource")
  // Define the shape of this stage, which is SourceShape with the port we defined above
  override val shape: SourceShape[Data] = SourceShape(out)

  // This is where the actual (possibly stateful) logic will live
  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {
      implicit val tm: Timeout = 1.second
      val actor = system.actorOf(Props(new Collector(sources)))

      def get1 = {
        Await.result((actor ? 1).mapTo[List[Data]], 2.second)
      }

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          push(out, get1.head)
        }
      })
    }
  }
}


class EventBusListener[T](system: ActorSystem) extends GraphStage[SourceShape[T]] {

  case object Halt

  class Collector2(filter: Class[_]) extends Actor with ActorLogging {

    context.system.eventStream.subscribe(self, filter)

    val queue = mutable.Queue.empty[T]

    override def receive = {
      case Halt =>
        context stop self
      case data: T =>
        queue.enqueue(data)
      case i: Int =>
        sender() ! List.fill(i)(Try(queue.dequeue()).toOption).collect { case Some(a) => a }
    }

    override def postStop() = {
      context.system.eventStream.unsubscribe(self)
      super.postStop()
    }
  }

  val out: Outlet[T] = Outlet("EventBus.out")
  override val shape: SourceShape[T] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    implicit val tm: Timeout = 1.second
    val actor = system.actorOf(Props(new Collector2(classOf[T])))

    def get1 = {
      Await.result((actor ? 1).mapTo[List[T]], 2.second)
    }

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, get1.head)
      }
    })

    override def postStop() = {
      actor ! Halt
      super.postStop()
    }
  }
}