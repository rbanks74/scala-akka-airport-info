package actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern._
import play.api.libs.json.JsObject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** Companion Object for Actor Messages **/
object IataReceiver {
  case class Done()
  case class Failed()
  case class DataReceived(results: Set[JsObject])
}

/** Actor to get the Iata codes from somewhere..., and then create a Controller Actor to process the list **/
class IataReceiver extends Actor with ActorLogging {
  import IataReceiver._
  log.info("IataReceiverActor Started")

  /** Simulate getting the list of codes elsewhere **/
  def getIataList = Future(List("PHL","RDU", "BWI", "JAX", "BOS", "DAL", "123"))

  pipe(getIataList) to self

  /** Create overridable factory method to stub out creation and behavior of IataController Actor for testing **/
  def controllerProps(stringList: List[String]): Props = Props(new IataController(stringList))

  def receive = {

    /** After successfully getting the List of Iata codes, create and pass the code list to the IataController **/
    case iataCodeList: List[String] => context.actorOf(controllerProps(iataCodeList), "IataController")

    /** Print the results, placeholder for sending results elsewhere **/
    case DataReceived(results) => results.foreach(println(_))

    case Failed => println("failed")

  }
}
