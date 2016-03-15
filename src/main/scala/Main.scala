import actors.IataReceiver
import actors.IataReceiver.ProcessIt
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}
import com.typesafe.scalalogging._


/** Program to retrieve the airport statuses from the FAA web feed **/
object Main extends App with LazyLogging {

  implicit val config = ConfigFactory.load()

  logger.debug("Getting IATA codes for data retrieval")
  /** Simulate getting the list of codes elsewhere **/
  def getIataList = Future(List("PHL","RDU", "BWI", "JAX", "BOS", "DAL", "123"))
  def getIataList2 = Future(List("ACK", "ACT"))
  def getIataList3 = Future(List("ABE", "DOV", "ACY", "DFW"))
  def combined = List(getIataList, getIataList2, getIataList3)

  val iataSystem = ActorSystem("IATA")
  val iataReceiver = iataSystem.actorOf(Props(new IataReceiver), "IataReceiver")

  combined foreach (_ onComplete {
      case Success(iataList) =>
        iataReceiver ! ProcessIt(iataList)
      case Failure(e) =>
       println(s"Error retrieving Data, message: $e")
  })


  /** Looking into Mongo Integration Here
  val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient("AirportDB")
  println(db.collectionNames())

  val doc1 = MongoDBObject("id" -> 1, "name" -> "Bill")
  val doc2 = MongoDBObject("id" -> 2, "name" -> "Charlie")
  val coll = db("AirportTestCollection")
  coll.insert(doc1)
  coll.insert(doc2)
  println(coll.count())
  coll.drop()
    
  mongoClient.close()

  **/

}
