package actors

import actors.IataController.Retrieve
import akka.actor.{Actor, ActorLogging, Stash}
import argonaut.Argonaut._
import play.api.libs.json.JsObject
import repos.IRecord
import repos.JsonConversionImplicits._
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
      log.info("Please wait, IataReceiverActor in running state...")
      stash()

    /** After receiving Json data transform it into an IRecord Instance **/
    case DataReceived(results) =>
      //results.foreach(println(_)) //context.parent ! Result(results) send these somewhere, maybe to DB

      val IRecordSet = results.map(_.toString().decodeOption[IRecord].get)
      IRecordSet.foreach(println(_))

      unstashAll()
      context.become(waiting)

    case Failed =>
      log.error("Error in running state.")
      context.stop(self)
  }
}
