package actors

import actors.IataDBTest.SerializeToDB
import akka.actor.{Actor, ActorLogging}
import argonaut.Argonaut._
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._
import play.api.libs.json.JsObject
import repos.IRecord
import repos.JsonConversionImplicits.IRecordDecodeJson


object IataDBTest {
  case class SerializeToDB(sj: Set[JsObject])
}

class IataDBTest extends Actor with ActorLogging {

  def receive = {

    case SerializeToDB(sj) =>
      log.info("dbActor starting...")

      // From set of Json objects to set of IRecord Instances, uses Argonaut
      val newIRecordSet: Set[IRecord] = sj.map(jsOb => jsOb.toString().decodeOption[IRecord].get)

      // From set of IRecord Instances to set of MongoDB Objects, uses salat
      val newDBObjects: Set[DBObject] = newIRecordSet.map(x => grater[IRecord]asDBObject x)
      newDBObjects.foreach(println(_))

      context.stop(self)

  }
}
