package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import play.api.libs.json.{JsObject, Json}

object IataController {
  case class Done()
  case class Failure()
  case class Get(s: String)
  case class Data(jsonData: JsObject)
  //case class
}

class IataController(iataList: List[String]) extends Actor with ActorLogging {
  import IataController._

  //var iataReceived = Set.empty[String]
  var children = Set.empty[ActorRef]


  for (x <- iataList) yield children += context.actorOf(Props(new IataGetter(x)))

  def receive = {

    case Data(data) => println(Json.prettyPrint(data))

    case IataGetter.Done =>
      children -= sender
      if (children.isEmpty) println("done...")

    case _ => println("")
  }

}
