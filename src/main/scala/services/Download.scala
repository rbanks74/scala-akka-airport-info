package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

trait DownloadClient {
  def downloadPage(iata: String): Future[String]
}

object Download extends DownloadClient {

  /** Function to go to the FAA website and download the content **/
  def downloadPage(iata: String): Future[String] = Future(Source.fromURL("http://services.faa.gov/airport/status/" + iata + "?format=application/json").mkString)
}
