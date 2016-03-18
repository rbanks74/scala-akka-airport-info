package actors

import actors.IataController.Retrieve
import actors.IataDBSuper.SerializeToDB
import akka.actor._
import play.api.libs.json.JsObject

/** Companion Object for Actor Messages **/
object IataReceiver {
  case object Failed
  case class DataReceived(results: Set[JsObject])
  case class ProcessIt(data: List[String])
  def props(dbActorPath: ActorPath): Props = Props(new IataReceiver(dbActorPath))
}

/** Actor to get the Iata codes from somewhere..., and then create a Controller Actor to process the list **/
class IataReceiver(dbPath: ActorPath) extends Actor with Stash with ActorLogging {
  import IataReceiver._

  log.debug("IataReceiverActor beginning task...")
  def receive = waiting


  /** Waiting state for the Reception Actor **/
  val waiting: Receive = {

    case ProcessIt(data: List[String]) =>
      val tempController = context.actorOf(IataController.props)
      tempController ! Retrieve(data)
      context.become(running)

    case _ => log.info("Unknown case")
  }


  /** Running state for the Reception Actor **/
  def running: Receive = {
    case ProcessIt(iataCodeList) =>
      stash()

    /** After receiving Json data, as JsObjects, send them to IataDBTest Actor **/
    case DataReceived(results) =>
      context.actorSelection(dbPath) ! SerializeToDB(results)
      unstashAll()
      context.become(waiting)

    case Failed =>
      log.error("Error in running state.")
      context.stop(self)
  }
}