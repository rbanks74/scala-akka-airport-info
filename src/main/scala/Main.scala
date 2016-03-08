import actors.IataReceiver
import actors.IataReceiver.ProcessIt
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/** Program to retrieve the airport statuses from the FAA web feed **/
object Main extends App {

  implicit val config = ConfigFactory.load()

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
}
