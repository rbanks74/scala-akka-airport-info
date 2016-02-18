import play.api.libs.json.{Json, JsValue}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.io.Source



object Main extends App {

  //println(Source.fromURL("http://services.faa.gov/airport/status/PHL?format=application/json").mkString)

  val iata = List("PHL","RDU", "BWI", "JAX", "BOS", "DAL")

  val url = "http://services.faa.gov/airport/status/PHL?format=application/json"

  def downloadPage(url: String): Future[String] = Future(Source.fromURL(url).mkString)

  downloadPage(url).foreach(println)
  //Thread.sleep(2000)

  val json: JsValue = Json.parse(Source.fromURL("http://services.faa.gov/airport/status/PHL?format=application/json").mkString)

  println(Json.prettyPrint(json))


}
