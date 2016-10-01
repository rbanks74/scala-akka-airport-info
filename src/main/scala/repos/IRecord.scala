package repos

import argonaut.DecodeJson
import com.mongodb.casbah.Imports.ObjectId
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}


object IRecord {
  implicit def IRecordDecodeJson: DecodeJson[IRecord] = {

    val _id = new ObjectId
    val _id2 = BSONObjectID

    DecodeJson(c => for {
      iataCode <- (c --\ "iataCode").as[String]
      state <- (c --\ "state").as[String]
      airportName <- (c --\ "airportName").as[String]
      status <- (c --\ "status").as[Status]
      time <- (c --\ "time").as[String]

    } yield IRecord(_id2.generate, iataCode, state, airportName, status, time))
  }

  implicit object IRecordBSONRead extends BSONDocumentReader[IRecord] {
    def read(b: BSONDocument): IRecord =
      
  }

  implicit object IRecordBSONWriter extends BSONDocumentWriter[IRecord] {
    def write(i: IRecord): BSONDocument =
      BSONDocument(
      "_id" -> i._id,
      "iataCode" -> i.iataCode,
      "state" -> i.state,
      "airportName" -> i.airportName,
      "status" -> i.status,
      "time" -> i.time
      )
  }




}

case class IRecord(
  _id: BSONObjectID,
  iataCode: String,
  state: String,
  airportName: String,
  status: Status,
  time: String
)


object Status {

  implicit def StatusDecodeJson: DecodeJson[Status] =
    DecodeJson(c => for {
      reason <- (c --\ "reason").as[Option[String]]
      closureBegin <- (c --\ "closureBegin").as[Option[String]]
      endTime <- (c --\ "endTime").as[Option[String]]
      minDelay <- (c --\ "minDelay").as[Option[String]]
      avgDelay <- (c --\ "avgDelay").as[Option[String]]
      maxDelay <- (c --\ "maxDelay").as[Option[String]]
      closureEnd <- (c --\ "closureEnd").as[Option[String]]
      trend <- (c --\ "trend").as[Option[String]]
      trend <- (c --\ "trend").as[Option[String]]
      sType <- (c --\ "type").as[Option[String]]

    } yield Status(reason, closureBegin, endTime, minDelay, avgDelay, maxDelay, closureEnd, trend, sType))
}

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