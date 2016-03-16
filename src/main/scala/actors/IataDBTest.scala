package actors

import actors.IataDBTest.SerializeToDB
import akka.actor.{Actor, ActorLogging, Status}
import argonaut.Argonaut._
import play.api.libs.json.JsObject
import repos.{IRecord, IataDAO}
import services.JsonConversionImplicits.IRecordDecodeJson
import com.mongodb.WriteConcern

import scala.util.Try


object IataDBTest {
  case class SerializeToDB(sj: Set[JsObject])
}

class IataDBTest extends Actor with ActorLogging {

  val dao = new IataDAO
  val wc = WriteConcern.UNACKNOWLEDGED

  def insertRecord(rec: IRecord) = Try(dao.insert(rec, wc))

  def receive = {

    case SerializeToDB(sj) =>
      log.info("dbActor starting...")

      // From set of Json objects to set of IRecord Instances, uses Argonaut
      val newIRecordSet: Set[IRecord] = sj.map(jsOb => jsOb.toString().decodeOption[IRecord].get)
      // dao.insert(newIRecordSet.head)

      context.stop(self)

    case Status.Failure(e) =>
      log.error(s"Error found in IataDBTest Actor:$e")
      context.stop(self)

    case _ =>
      log.error("Something is amiss...")
      context.stop(self)

  }
}


/**
import actors.IataDBTest.SerializeToDB
import akka.actor.{Status, Actor, ActorLogging}
import argonaut.Argonaut._
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import play.api.libs.json.JsObject
import repos.IRecord
import repos.IataDAO
import services.JsonConversionImplicits
import JsonConversionImplicits.IRecordDecodeJson


      // From set of IRecord Instances to set of MongoDB Objects, uses salat
      val newDBObjects: Set[DBObject] = newIRecordSet.map(x => grater[IRecord]asDBObject x)
      //newDBObjects.foreach(println(_))
**/
