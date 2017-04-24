package io.github.morgaroth.eventsmanger

import com.typesafe.config.Config
import org.flywaydb.core.Flyway

import scala.util.Try

/**
  * Created by morgaroth on 18.10.16.
  */
case class Migrator(fw: Flyway, enabled: Boolean) {
  def migrate() = if (enabled) fw.migrate() else -1
}

trait FlyWayEvolutions {
  def initMigrator(cfg: Config) = {
    val flyway = new Flyway()
    val user = cfg.getString("properties.user")
    val password = cfg.getString("properties.password")
    val dbName = cfg.getString("properties.databaseName")
    val server = cfg.getString("properties.serverName")
    val port = cfg.getInt("properties.portNumber")
    val url = s"jdbc:postgresql://$server:$port/$dbName"
    val enabled = Try(cfg.getBoolean("evolutions")).getOrElse(true)
    flyway.setDataSource(url, user, password)
    flyway.setBaselineOnMigrate(true)
    Migrator(flyway, enabled)
  }

  def migrate(cfg: Config) = {
    initMigrator(cfg).migrate()
  }
}

object FlyWayEvolutions extends FlyWayEvolutions