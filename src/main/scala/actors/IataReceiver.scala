package actors

import actors.IataController.Retrieve
import actors.IataDBTest.SerializeToDB
import akka.actor._
import play.api.libs.json.JsObject
import services.IataProps.iataControllerProps

/** Companion Object for Actor Messages **/
object IataReceiver {
  case object Failed
  case class ActorRefSet(refs: ActorRefHolder)
  case class DataReceived(results: Set[JsObject])
  case class ProcessIt(data: List[String])
  case class Result(result: Set[JsObject]) //For main, one day maybe
}

/** Actor to get the Iata codes from somewhere..., and then create a Controller Actor to process the list **/
class IataReceiver extends Actor with Stash with ActorLogging {
  import IataReceiver._

  log.debug("IataReceiverActor beginning task...")
  var dbActorPath: ActorPath = self.path

  def receive = waiting


  /** Waiting state for the Reception Actor **/
  val waiting: Receive = {
    case ProcessIt(data: List[String]) =>
      val tempController = context.actorOf(iataControllerProps)
      tempController ! Retrieve(data)
      context.become(running)

    case ActorRefSet(refs) =>
      dbActorPath = refs.refMap.get("dbActorPath").get

    case _ => log.info("Unknown case")
  }


  /** Running state for the Reception Actor **/
  def running: Receive = {
    case ProcessIt(iataCodeList) =>
      stash()

    /** After receiving Json data, as JsObjects, send them to IataDBTest Actor **/
    case DataReceived(results) =>
      context.actorSelection(dbActorPath) ! SerializeToDB(results)

      unstashAll()
      context.become(waiting)

    case Failed =>
      log.error("Error in running state.")
      context.stop(self)
  }

}

/** Case Class to hold the ActorPath's for specified Actors **/
case class ActorRefHolder(refMap: Map[String, ActorPath])