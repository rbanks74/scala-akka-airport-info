package actors

import actors.IataReceiver.DataReceived
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import play.api.libs.json.JsObject


/** Companion Object for Actor Messages **/
object IataController {
  case class Done()
  case class Failure()
  case class Get(s: String)
  case class Data(jsonData: JsObject)
}


/** Actor to receive the List of Iata codes to delegate to child actors, then gathers the results and sends them to the IataReceiver Actor **/
class IataController(iataList: List[String]) extends Actor with ActorLogging {
  import IataController._

  log.info("IataControllerActor Started")
  var iataReceived: Set[JsObject] = Set.empty[JsObject]
  var children = Set.empty[ActorRef]


  /** For each Iata Code, add the child actor to the children set, and use child actor to process each request for content **/
  for (code <- iataList) yield children += context.actorOf(Props(new IataGetter(code)))


  /** Handle the Failure cases **/
  def stop() = {
    log.info(s"error with: $self")
    context.parent ! Done
    context.stop(self)
  }

  def receive = {

    /** Add the processed content to the iataReceived set **/
    case Data(data) =>
      //println(Json.prettyPrint(data))
      iataReceived += data

    /** To ensure all child actors are accounted for, and send the iataReceived set to the IataReceiver Actor when completed **/
    case IataGetter.Done =>
      children -= sender
      if (children.isEmpty) context.parent ! DataReceived(iataReceived)

    /** Failure Cases **/
    //case _ => stop()
    case Failure => stop()
  }
}
