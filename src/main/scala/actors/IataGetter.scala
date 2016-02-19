package actors

import java.util.Calendar

import actors.IataController.Data
import akka.actor.{Actor, ActorLogging, Status}
import akka.pattern._
import play.api.libs.json.Json._
import play.api.libs.json.{JsObject, JsString}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object IataGetter {
  case class Failed()
  case class Done()
}


class IataGetter(iata: String) extends Actor with ActorLogging {
  import IataGetter._

  def downloadPage: Future[String] = Future(Source.fromURL("http://services.faa.gov/airport/status/" + iata + "?format=application/json").mkString)

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
    cleansedData
  }

  pipe(downloadPage) to self

  def stop(): Unit = {
    log.info(s"error with: $iata")
    //context.parent ! Done
    //context.stop(self)
  }

  def receive = {

    case body: String =>
      log.info(s"{} Successfully Retrieved url content for $iata.", self)
      context.parent ! Data(toJsonAndTransform(body))

    case Failed => stop()

    case _: Status.Failure => stop()

  }
}
