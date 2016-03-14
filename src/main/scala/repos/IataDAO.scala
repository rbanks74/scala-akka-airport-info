package repos

import com.mongodb.casbah._
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import com.mongodb.casbah.Imports.ObjectId

object IataDAO extends SalatDAO[IRecord, ObjectId](collection = MongoClient("localhost")("IataTest").apply("Iatas"))
