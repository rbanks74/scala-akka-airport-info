package repos

import com.mongodb.casbah.Imports._

/** Case Classes to pass data into DB **/

case class IRecordQueryParams(
  iataCode: Option[String] = None,
  state: Option[String] = None,
  airportName: Option[String] = None,
  status: Option[Status] = None,
  time: Option[String] = None
)

case class IRecord(
  id: ObjectId,
  iataCode: String,
  state: String,
  airportName: String,
  status: Status,
  time: String
)

case class Status(
  reason: Option[String],
  closureBegin: Option[String],
  endTime: Option[String],
  minDelay: Option[String],
  avgDelay: Option[String],
  maxDelay: Option[String],
  closureEnd: Option[String],
  trend: Option[String],
  sType: Option[String]
)