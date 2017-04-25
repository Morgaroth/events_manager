package io.github.morgaroth.eventsmanger

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Stash}
import akka.pattern.ask
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Promise, TimeoutException}
import scala.reflect.{ClassTag, _}
import scala.util.Success

object EventBusSource {
  def apply[T: ClassTag](implicit system: ActorSystem): EventBusSource[T] = new EventBusSource[T](system)
}

class EventBusSource[T: ClassTag](system: ActorSystem) extends GraphStage[SourceShape[T]] {

  case object Halt

  case object Dispose

  case object Get

  class Collector2(filter: Class[_]) extends Actor with ActorLogging with Stash {

    context.system.eventStream.subscribe(self, filter)

    val queue = mutable.Queue.empty[T]
    val queueReq = mutable.Queue.empty[Promise[T]]

    override def receive = {
      case Halt =>
        context stop self
      case data: T =>
        queue.enqueue(data)
        unstashAll()
      case Get if queue.nonEmpty =>
        sender() ! Promise.successful(queue.dequeue())
      case Get =>
        val prom = Promise[T]
        queueReq.enqueue(prom)
        sender() ! prom
        self ! Dispose
      case Dispose if queue.isEmpty =>
        stash()
      case Dispose =>
        queueReq.dequeue().complete(Success(queue.dequeue()))
    }

    override def postStop() = {
      context.system.eventStream.unsubscribe(self)
      super.postStop()
    }
  }

  val out: Outlet[T] = Outlet("EventBus.out")
  override val shape: SourceShape[T] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    implicit val tm: Timeout = 100.millis
    val actor = system.actorOf(Props(new Collector2(classTag[T].runtimeClass)))

    import system.dispatcher

    def askElement: T = Await.result((actor ? Get).map {
      case i: T => i
      case p: Promise[T] => waitForever(p)
    }, 250.days)

    setHandler(out, new OutHandler {
      override def onPull(): Unit = {
        push(out, askElement)
      }
    })

    override def postStop() = {
      actor ! Halt
      super.postStop()
    }
  }

  def waitForever[A](p: Promise[A])(implicit ex: ExecutionContext): A = {
    try {
      Await.result(p.future, 10.seconds)
    } catch {
      case _: TimeoutException =>
        waitForever(p)
    }
  }
}
