import actors.IataReceiver.{ActorPathSet, ProcessIt}
import actors.{IataDBTest, ActorPathHolder, IataReceiver}
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


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
  val iataReceiver = iataSystem.actorOf(IataReceiver.props, "IataReceiver")
  val dbActor = iataSystem.actorOf(IataDBTest.props, "IataDBTest")


  val actorRefs: ActorPathHolder = new ActorPathHolder(Map("dbActorPath" -> dbActor.path, "receiverActorPath" -> iataReceiver.path))
  iataReceiver ! ActorPathSet(actorRefs)

  combined foreach (_ onComplete {
      case Success(iataList) =>
        iataReceiver ! ProcessIt(iataList)
      case Failure(e) =>
        logger.error(s"Error retrieving Data, message: $e")
  })


  sys.addShutdownHook {
    iataSystem.log.info("Shutting down")
    iataSystem.shutdown()
    iataSystem.awaitTermination()
    logger.info(s"Actor system '${iataSystem.name}' successfully shut down")
  }

}
