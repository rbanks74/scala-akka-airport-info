package services

import argonaut.Argonaut._
import repos.{IRecord, Status}
import services.JsonConversionImplicits.IRecordDecodeJson
import services.ToJson._


object ClassHelper {

  def makeIRecord: IRecord = {
    //val x = toJsonAndTransform(data).get  // Returns a Try without .get
    //val y: IRecord = x.toString().decodeOption[IRecord].get // Returns an Option[IRecord] without .get

    val data = "{\"delay\":\"false\",\"IATA\":\"IAD\",\"state\":\"District of Columbia\",\"name\":\"Washington Dulles International\",\"weather\":{\"visibility\":2.00,\"weather\":\"Rain Fog/Mist\",\"meta\":{\"credit\":\"NOAA's National Weather Service\",\"updated\":\"8:52 AM Local\",\"url\":\"http://weather.gov/\"},\"temp\":\"38.0 F (3.3 C)\",\"wind\":\"North at 8.1mph\"},\"ICAO\":\"KIAD\",\"city\":\"Washington\",\"status\":{\"reason\":\"No known delays for this airport.\",\"closureBegin\":\"\",\"endTime\":\"\",\"minDelay\":\"\",\"avgDelay\":\"\",\"maxDelay\":\"\",\"closureEnd\":\"\",\"trend\":\"\",\"type\":\"\"}} "
    val toIRecord: IRecord = toJsonAndTransform(data).map(jsOb => jsOb.toString().decodeOption[IRecord].get).get
    toIRecord.copy(status = makeStatus)
  }

  def makeStatus: Status = new Status(Some(""),Some(""),Some(""),Some(""),Some(""),Some(""),Some(""),Some(""),Some(""))

}
