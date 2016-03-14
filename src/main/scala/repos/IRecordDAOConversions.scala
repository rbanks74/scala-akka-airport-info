package repos

import com.mongodb.DBObject
import com.novus.salat._
import com.novus.salat.global._
import scala.language.implicitConversions


object IRecordDAOConversions {
  implicit def paramsToDBObject(params: IRecordQueryParams): DBObject =
    grater[IRecordQueryParams].asDBObject(params)

  implicit def irecordToDBObject(i: IRecord): DBObject =
    grater[IRecord].asDBObject(i)
}