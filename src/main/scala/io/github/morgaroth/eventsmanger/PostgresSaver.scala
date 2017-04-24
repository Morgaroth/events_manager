package io.github.morgaroth.eventsmanger

import java.util.UUID

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import com.typesafe.config.Config
import io.github.morgaroth.eventsmanger.BetBlocksPostgresDriver.api._
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

case class Window(start: DateTime, end: DateTime, kind: WindowKind, state: WindowState = NotChecked, tags: List[String] = List.empty, id: UUID = UUID.randomUUID)

object Window {
  def present(start: DateTime, end: DateTime) = Window(start, end, kind = Present)

  def absent(start: DateTime, end: DateTime) = Window(start, end, kind = Absent)
}

//@formatter:off
sealed trait WindowKind { def state: String }
case object Present extends WindowKind { val state = "present" }
case object Absent extends WindowKind { val state = "absent" }
case object Coffee extends WindowKind { val state = "coffee" }
case object Meeting extends WindowKind { val state = "meeting" }
object WindowTypes {
  private val all = List(Present, Absent, Coffee, Meeting)
  private val map = all.map(x => x.state -> x).toMap
  def apply(name: String) = map(name)
}

sealed trait WindowState { def state: String }
case object Checked extends WindowState { val state = "checked" }
case object NotChecked extends WindowState { val state = "not-checked" }
object WindowStates {
  private val all = List(Checked, NotChecked)
  private val map = all.map(x => x.state -> x).toMap
  def apply(name: String) = map(name)
}
//@formatter:on

trait CustomMappers {
  implicit val wnindowKindMapper = MappedColumnType.base[WindowKind, String](e => e.state, s => WindowTypes(s))
  implicit val wnindowStateMapper = MappedColumnType.base[WindowState, String](e => e.state, s => WindowStates(s))
}

class Windows(tag: Tag) extends Table[Window](tag, "times") with CustomMappers {
  def id = column[UUID]("id", O.PrimaryKey)

  def start = column[DateTime]("start")

  def end = column[DateTime]("end")

  def state = column[WindowState]("state")

  def kind = column[WindowKind]("kind")

  def tags = column[List[String]]("tags")

  override def * = (start, end, kind, state, tags, id) <> ((Window.apply _).tupled, Window.unapply)
}

object db {
  val driver = MyPostgresDriver

  val windows = TableQuery[Windows]

  val allTables = List(windows)

  def schema(): driver.DDL = {
    import driver.api._
    allTables.map(_.schema).reduce(_ ++ _)
  }
}


case class PostgresSaver(config: Config)(implicit ex: ExecutionContext) extends GraphStage[FlowShape[Window, Window]] {

  val in = Inlet[Window]("PostgresSaver.in")
  val out = Outlet[Window]("PostgresSaver.out")

  val shape = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      private val DB = Database.forConfig("", config)
      FlyWayEvolutions.migrate(config)

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val elem = grab(in)
          DB.run(db.windows += elem).onComplete {
            case Success(_) => push(out, elem)
            case Failure(t) =>
              log.error(t, s"error during save window $elem")
          }
        }

        override def onUpstreamFinish(): Unit = {
          DB.close()
          complete(out)
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}