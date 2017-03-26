package io.github.morgaroth.eventsmanger

import java.util.UUID

import akka.stream.scaladsl.Flow
import com.typesafe.config.Config
import io.github.morgaroth.eventsmanger.BetBlocksPostgresDriver.api._
import org.joda.time.DateTime

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
  val windows = TableQuery[Windows]
}


class PostgresSaver(config: Config) {
  private val DB = Database.forConfig("", config)

  val flow = Flow[Window].mapAsync(5)(x => DB.run(db.windows += x).map(_ => x))
}