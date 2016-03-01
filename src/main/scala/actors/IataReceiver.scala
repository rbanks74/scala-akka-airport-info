package actors

import akka.actor.{Actor, ActorLogging, Stash}
import play.api.libs.json.JsObject
import services.IataProps.iataControllerProps

/** Companion Object for Actor Messages **/
object IataReceiver {
  case object Failed
  case class DataReceived(results: Set[JsObject])
  case class ProcessIt(data: List[String])
  case class Result(result: Set[JsObject])
}

/** Actor to get the Iata codes from somewhere..., and then create a Controller Actor to process the list **/
class IataReceiver extends Actor with Stash with ActorLogging {
  import IataReceiver._
  log.info("IataReceiverActor Started...")

  def receive = waiting

  /** Create states for the Reception Actor **/
  val waiting: Receive = {
    case ProcessIt(data: List[String]) =>
      context.actorOf(iataControllerProps(data))
      context.become(running)

    case _ => log.info("Unknown case")
  }

  def running: Receive = {
    case ProcessIt(iataCodeList: List[String]) =>
      log.info("Please wait, IataReceiverActor in running state...")
      stash()

    case DataReceived(results) =>
      results.foreach(println(_)) //context.parent ! Result(results)
      unstashAll()
      context.become(waiting)

    case Failed =>
      log.info("Error in running state.")
      context.stop(self)
  }
}
