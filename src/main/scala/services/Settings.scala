package services

import com.typesafe.config.{ConfigFactory, Config}


class Settings(val config: Config) {
  val airportSettings = config.getConfig("airportStatusApp")
  import airportSettings._

  val logLevel = getString("airportStatusApp.akka.loglevel")
  println(logLevel)

}

object Settings extends Settings(ConfigFactory.load)
