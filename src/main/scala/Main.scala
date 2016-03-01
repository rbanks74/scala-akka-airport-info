import actors.IataReceiver
import actors.IataReceiver.{ProcessIt, Result}
import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Failure}

/** Program to retrieve the airport statuses from the FAA web feed **/
object Main extends App {

  val config = ConfigFactory.load()

  /** Simulate getting the list of codes elsewhere **/
  def getIataList = Future(List("PHL","RDU", "BWI", "JAX", "BOS", "DAL", "123"))
  def getIataList2 = Future(List("ACK", "ACT"))
  def getIataList3 = Future(List("ABE", "ACB", "ACY", "ADM"))
  def combined = List(getIataList, getIataList2, getIataList3)

  val iataSystem = ActorSystem("IATA")
  val iataReceiver = iataSystem.actorOf(Props(new IataReceiver), "IataReceiver")

  combined foreach (_ onComplete {
      case Success(iataList) =>
        iataReceiver ! ProcessIt(iataList)
      case Failure(e) =>
        println(s"Error retrieving Data, message: $e")
  })

  def receive: Receive = {
    case Result(results: Set[JsObject]) => results.foreach(println(_))
  }

}
