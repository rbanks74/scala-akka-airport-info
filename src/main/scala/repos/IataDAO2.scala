package repos

import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.QueryOpts
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.Count
import services.Settings._
import scala.concurrent.ExecutionContext.Implicits.global

trait IataDAO2 {

  val collection = db[BSONCollection]("Iatas")

  def save(i: IRecord) = collection.insert(i)

  def findByCode() = {}
}
