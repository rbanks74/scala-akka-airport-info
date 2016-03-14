package actors

import actors.IataController.Retrieve
import actors.IataDBTest.SerializeToDB
import akka.actor.{Props, Actor, ActorLogging, Stash}
import play.api.libs.json.JsObject
import services.IataProps.iataControllerProps

/** Companion Object for Actor Messages **/
object IataReceiver {
  case object Failed
  case class DataReceived(results: Set[JsObject])
  case class ProcessIt(data: List[String])
  case class Result(result: Set[JsObject]) //For main, one day maybe
}

/** Actor to get the Iata codes from somewhere..., and then create a Controller Actor to process the list **/
class IataReceiver extends Actor with Stash with ActorLogging {
  import IataReceiver._
  log.info("IataReceiverActor beginning task...")

  def receive = waiting

  /** Create states for the Reception Actor **/
  val waiting: Receive = {
    case ProcessIt(data: List[String]) =>
      val tempController = context.actorOf(iataControllerProps)
      tempController ! Retrieve(data)
      context.become(running)

    case _ => log.info("Unknown case")
  }

  def running: Receive = {
    case ProcessIt(iataCodeList) =>
      log.debug("Please wait, IataReceiverActor in running state...")
      stash()

    /** After receiving Json data, as JsObjects, transform them into an IRecord Instances **/
    case DataReceived(results) =>
      log.debug("receiver has results now!")

      val dbActor = context.actorOf(Props(new IataDBTest))
      dbActor ! SerializeToDB(results)

      unstashAll()
      context.become(waiting)

    case Failed =>
      log.error("Error in running state.")
      context.stop(self)
  }
}
