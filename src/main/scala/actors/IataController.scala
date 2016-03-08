package actors

import actors.IataGetter.Process
import actors.IataReceiver.DataReceived
import akka.actor.{Actor, ActorLogging, ActorRef}
import play.api.libs.json.{Json, JsObject}
import services.IataProps.iataGetterProps


/** Companion Object for Actor Messages **/
object IataController {
  case class Done()
  case class Failure()
  case class Retrieve(s: List[String])
  case class Data(t: JsObject)
}


/** Actor to receive the List of Iata codes to delegate to child actors, then gathers the results and sends them to the IataReceiver Actor **/
class IataController extends Actor with ActorLogging {
  import IataController._

  log.info("IataControllerActor beginning task...")
  var iataReceived: Set[JsObject] = Set.empty[JsObject]
  var children = Set.empty[ActorRef]


  /** Handle the Failure cases **/
  def stop() = {
    log.info(s"error with: $self")
    context.parent ! Done
    context.stop(self)
  }

  def receive = {

    case Retrieve(codeList: List[String]) =>
      for (x <- codeList) yield {
        var tempGetter = context.actorOf(iataGetterProps)
        tempGetter ! Process(x)
        children += tempGetter
      }

    /** Add the processed content to the iataReceived set **/
    case Data(data) =>
      log.debug(Json.prettyPrint(data))
      iataReceived += data

    /** To ensure all child actors are accounted for, and send the iataReceived set to the IataReceiver Actor when completed **/
    case IataGetter.Done =>
      children -= sender
      if (children.isEmpty) {
        context.parent ! DataReceived(iataReceived)
        context.stop(self)
      }

    case Failure => stop()
  }
}
