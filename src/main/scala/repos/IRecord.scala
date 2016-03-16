package repos

import com.mongodb.casbah.Imports.ObjectId

/** Case Classes to pass data into DB **/
case class IRecord(
  _id: ObjectId,
  iataCode: String,
  state: String,
  airportName: String,
  status: Status,
  time: String
)

/**
object IRecord extends ModelCompanion[IRecord, ObjectId] {
  val db: MongoDB = MongoClient("localhost")("iatas")
  val coll: MongoCollection = db.apply("iataTable")
  val dao = new SalatDAO[IRecord, ObjectId](collection = coll) {}
}
**/

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

case class IRecordQueryParams(
  _id: Option[ObjectId] = None,
  iataCode: Option[String] = None,
  state: Option[String] = None,
  airportName: Option[String] = None,
  status: Option[Status] = None,
  time: Option[String] = None
)