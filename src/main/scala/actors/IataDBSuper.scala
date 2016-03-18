package actors

import akka.actor.{Props, Actor, ActorLogging, Status}
import argonaut.Argonaut._
import com.mongodb.WriteConcern
import play.api.libs.json.JsObject
import repos.{IRecord, IataDAO}
import com.novus.salat._
import com.novus.salat.global._
import services.JsonConversionImplicits.IRecordDecodeJson
import scala.util.Try
import com.mongodb.casbah.Imports._


object IataDBSuper {
  case class SerializeToDB(sj: Set[JsObject])
  def props: Props = Props(new IataDBSuper)
}

class IataDBSuper extends Actor with ActorLogging {
  import IataDBSuper._

  val dao = new IataDAO
  val wc = WriteConcern.UNACKNOWLEDGED
  def insertRecord(rec: IRecord) = Try(dao.insert(rec, wc))


  def receive = {

    case SerializeToDB(sj) =>
      log.info(s"$self: dbActor starting...")

      // From set of Json objects to set of IRecord Instances, uses Argonaut
      val newIRecordSet: Set[IRecord] = sj.map(jsOb => jsOb.toString().decodeOption[IRecord].get)

      // From set of IRecord Instances to set of MongoDB Objects, uses salat
      val newDBObjects: Set[DBObject] = newIRecordSet.map(x => grater[IRecord]asDBObject x)
      newDBObjects.foreach(println(_))

    case Status.Failure(e) =>
      log.error(s"Error found in IataDBSuper Actor:$e")
      context.stop(self)

    case _ =>
      log.error("Something is amiss...")
      context.stop(self)

  }
}
