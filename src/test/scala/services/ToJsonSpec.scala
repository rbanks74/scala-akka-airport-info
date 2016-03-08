package services

import java.util.NoSuchElementException
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import play.api.libs.json.JsObject
import services.ToJson.toJsonAndTransform
import scala.util.Try


class ToJsonSpec extends FlatSpec {

  val data = "{\"delay\":\"false\",\"IATA\":\"IAD\",\"state\":\"District of Columbia\",\"name\":\"Washington Dulles International\",\"weather\":{\"visibility\":2.00,\"weather\":\"Rain Fog/Mist\",\"meta\":{\"credit\":\"NOAA's National Weather Service\",\"updated\":\"8:52 AM Local\",\"url\":\"http://weather.gov/\"},\"temp\":\"38.0 F (3.3 C)\",\"wind\":\"North at 8.1mph\"},\"ICAO\":\"KIAD\",\"city\":\"Washington\",\"status\":{\"reason\":\"No known delays for this airport.\",\"closureBegin\":\"\",\"endTime\":\"\",\"minDelay\":\"\",\"avgDelay\":\"\",\"maxDelay\":\"\",\"closureEnd\":\"\",\"trend\":\"\",\"type\":\"\"}} "
  val tryResult: Try[JsObject] = toJsonAndTransform(data)
  val result: JsObject = tryResult.get

  val badData = "{\"delay\":\"false\",\"state\":\"District of Columbia\",\"name\":\"Washington Dulles International\",\"weather\":{\"visibility\":2.00,\"weather\":\"Rain Fog/Mist\",\"meta\":{\"credit\":\"NOAA's National Weather Service\",\"updated\":\"8:52 AM Local\",\"url\":\"http://weather.gov/\"},\"temp\":\"38.0 F (3.3 C)\",\"wind\":\"North at 8.1mph\"},\"ICAO\":\"KIAD\",\"city\":\"Washington\",\"status\":{\"reason\":\"No known delays for this airport.\",\"closureBegin\":\"\",\"endTime\":\"\",\"minDelay\":\"\",\"avgDelay\":\"\",\"maxDelay\":\"\",\"closureEnd\":\"\",\"trend\":\"\",\"type\":\"\"}} "
  val badResult = toJsonAndTransform(badData)


  "ToJsonAndTransform" should "return a JSON Object" in {
    assert(classOf[JsObject] == result.getClass)
  }

  "ToJsonAndTransform" should "have the same fields as listed below" in {
    assert(result.keys.toList == List("iataCode", "state", "airportName", "status", "time"))
  }

  "ToJsonAndTransform" should "fail when given bad data" in {
    a [NoSuchElementException] should be thrownBy badResult.get
  }
}
