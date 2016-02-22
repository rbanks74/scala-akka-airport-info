package actors

import java.util.Calendar
import actors.IataController.Data
import akka.actor.{Actor, ActorLogging, Status}
import akka.pattern._
import play.api.libs.json.Json
import play.api.libs.json.{JsObject, JsString}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source


/**  Companion Object for Actor Messages  **/
object IataGetter {
  case class Failed()
  case class Done()
}

/** Actor to Download and Process each IATA code received and send result to the IataController **/
class IataGetter(iata: String) extends Actor with ActorLogging {
  import IataGetter._


  /** Function to go to the FAA website and download the content **/
  def downloadPage: Future[String] = Future(Source.fromURL("http://services.faa.gov/airport/status/" + iata + "?format=application/json").mkString)


  /** Function to transform the string content to a Json Object with only relevant data **/
  def toJsonAndTransform(data: String): JsObject = {
    val pageData = Json.parse(data)
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


  /** Pipe down the original content into the actor for processing **/
  pipe(downloadPage) to self


  /** Function to catch any errors during Actor processing **/
  def stop(): Unit = {
    log.info(s"error with: $iata")
    context.parent ! Done
    context.stop(self)
  }


  def receive = {

    /** Processes each of the downloaded content, transforms it, and sends it to the controller **/
    case body: String =>
      log.info(s"{} Successfully Retrieved url content for $iata.", self)
      context.parent ! Data(toJsonAndTransform(body))
      context.parent ! Done
      context.stop(self)

    /** Failure Cases **/
    case Failed => stop()
    case _: Status.Failure => stop()
  }
}
