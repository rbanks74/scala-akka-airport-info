package repos

/** Case Classes to pass data into DB **/
case class IRecord(
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