package services

import java.util.Calendar
import play.api.libs.json.{JsString, Json, JsObject}

import scala.util.Try


object ToJson {

  /** Function to transform the string content to a Json Object with only relevant data **/
  def toJsonAndTransform(data: String): Try[JsObject] = Try {
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
}
