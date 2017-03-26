package io.github.morgaroth.eventsmanger

import com.github.tminglei.slickpg.{ExPostgresProfile, PgArraySupport, PgDateSupportJoda}
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

trait BetBlocksPostgresDriver extends ExPostgresProfile
  with PgArraySupport
  with PgDateSupportJoda {

  override val api = MyAPI

  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  object MyAPI extends API with ArrayImplicits with DateTimeImplicits

}

object BetBlocksPostgresDriver extends BetBlocksPostgresDriver
