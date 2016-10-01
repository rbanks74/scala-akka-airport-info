package services

import com.typesafe.config.{ConfigFactory, Config}
import reactivemongo.api.MongoDriver
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global


class Settings(val config: Config) {

  val airportSettings = config.getConfig("airportStatusApp")
  import airportSettings._

  val logLevel = getString("airportStatusApp.akka.loglevel")

  val database = getString("mongodb.database")
  val servers = getStringList("mongodb.servers").asScala

  val driver = new MongoDriver()
  val connection = driver.connection(servers)
  val db = connection(database)

}

object Settings extends Settings(ConfigFactory.load)
