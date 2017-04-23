package io.github.morgaroth.eventsmanger

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern.ask
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.util.Timeout

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object EventBusSource {
  def apply[T](implicit system: ActorSystem): EventBusSource[T] = new EventBusSource[T](system)
}

class EventBusSource[T](system: ActorSystem) extends GraphStage[SourceShape[T]] {

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
