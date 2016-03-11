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
      val newIRecordSet: Set[IRecord] = sj.map(j => j.toString().decodeOption[IRecord].get)
      val newDBObjects: Set[DBObject] = newIRecordSet.map(x => grater[IRecord]asDBObject x)
      newDBObjects.foreach(println(_))


  }

}
