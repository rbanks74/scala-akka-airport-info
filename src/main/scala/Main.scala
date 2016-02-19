import java.util.Calendar

import play.api.libs.json.Json._
import play.api.libs.json.{JsObject, JsString, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Source


object Main extends App {

  val iata = List("PHL","RDU", "BWI", "JAX", "BOS", "DAL")
  val url = "http://services.faa.gov/airport/status/PHL?format=application/json"

  def downloadPage(url: String): Future[String] = Future(Source.fromURL(url).mkString)

  def toJsonAndTransform(data: String): JsObject = {
    val pageData = parse(data)
    val currentTimeStamp = Calendar.getInstance().getTime

    val cleansedData = JsObject(Seq(
      "iataCode" -> (pageData \ "IATA").get,
      "state" -> (pageData \ "state").get,
      "airportName" -> (pageData \ "name").get,
      "status" -> (pageData \ "status").get,
      "time" -> JsString(currentTimeStamp.toString)
    ))

    println(Json.prettyPrint(cleansedData))
    cleansedData
  }

  val json = downloadPage(url).map(urlContent => toJsonAndTransform(urlContent))
  Thread.sleep(2000)


}
