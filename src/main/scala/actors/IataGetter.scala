package actors

import actors.IataController.Data
import akka.actor.{Actor, ActorLogging, Status}
import services.Download
import services.ToJson.toJsonAndTransform
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


/**  Companion Object for Actor Messages  **/
object IataGetter {
  case class Process(code: String)
  case class Failed()
  case class Done()
}

/** Actor to Download and Process each IATA code received and send result to the IataController **/
class IataGetter extends Actor with ActorLogging {
  import IataGetter._


  /** Function to catch any errors during Actor processing **/
  def stop(): Unit = {
    context.parent ! Done
    context.stop(self)
  }


  def receive = {

    /** Processes Iata code; downloads the content, transforms it, and sends it to the controller **/
    case Process(code) =>
      Download.downloadPage(code).onComplete {

        case Success(content) =>
          log.info(s"{} Successfully Retrieved url content for $code.", self)
          //toJsonAndTransform(content).map(x => Data(x))
          context.parent ! toJsonAndTransform(content).map(x => Data(x))//
          stop()

        case Failure(e) =>
          log.error(s"Error getting data for $code: $e")
          stop()
      }

    /** Failure Cases **/
    case Failed => stop()
    case _: Status.Failure => stop()
  }
}
