package repos

import com.mongodb.DBObject
import com.mongodb.casbah.Imports.ObjectId
import com.mongodb.casbah._
import com.novus.salat._
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._

import scala.language.implicitConversions

class IataDAO extends SalatDAO[IRecord, ObjectId](collection = MongoClient("localhost")("IataTest").apply("Iatas"))


object otherImplicitConversions {
  implicit def iRecordToDBObject(i: IRecord): DBObject =
    grater[IRecord].asDBObject(i)
}