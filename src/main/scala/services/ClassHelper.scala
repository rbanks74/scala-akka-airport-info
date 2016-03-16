package services

import argonaut.Argonaut._
import play.api.libs.json.JsObject
import repos.{IRecord, Status}
import services.JsonConversionImplicits.IRecordDecodeJson
import services.ToJson._

import scala.util.Try


object ClassHelper {

  def makeIRecord: IRecord = {
    val data = "{\"delay\":\"false\",\"IATA\":\"IAD\",\"state\":\"District of Columbia\",\"name\":\"Washington Dulles International\",\"weather\":{\"visibility\":2.00,\"weather\":\"Rain Fog/Mist\",\"meta\":{\"credit\":\"NOAA's National Weather Service\",\"updated\":\"8:52 AM Local\",\"url\":\"http://weather.gov/\"},\"temp\":\"38.0 F (3.3 C)\",\"wind\":\"North at 8.1mph\"},\"ICAO\":\"KIAD\",\"city\":\"Washington\",\"status\":{\"reason\":\"No known delays for this airport.\",\"closureBegin\":\"\",\"endTime\":\"\",\"minDelay\":\"\",\"avgDelay\":\"\",\"maxDelay\":\"\",\"closureEnd\":\"\",\"trend\":\"\",\"type\":\"\"}} "
    val tryResult: Try[JsObject] = toJsonAndTransform(data)
    val result: JsObject = tryResult.get
    val toIRecord = result.toString().decodeOption[IRecord].get
    toIRecord.copy(status = makeStatus)
  }

  def makeStatus: Status = new Status(Some(""),Some(""),Some(""),Some(""),Some(""),Some(""),Some(""),Some(""),Some(""))

}
