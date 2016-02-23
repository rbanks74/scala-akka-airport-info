package actors

import actors.IataController.Data
import akka.actor.{Actor, ActorLogging, Status}
import akka.pattern._
import services.Download
import services.ToJson.toJsonAndTransform

import scala.concurrent.ExecutionContext.Implicits.global


/**  Companion Object for Actor Messages  **/
object IataGetter {
  case class Failed()
  case class Done()
}

/** Actor to Download and Process each IATA code received and send result to the IataController **/
class IataGetter(iata: String) extends Actor with ActorLogging {
  import IataGetter._


  /** Adding layer to stub out the data for testing **/
  def downloadClient = Download.downloadPage(iata)


  /** Pipe down the original content into the actor for processing **/
  pipe(downloadClient) to self


  /** Function to catch any errors during Actor processing **/
  def stop(): Unit = {
    log.info(s"error with: $iata")
    context.parent ! Done
    context.stop(self)
  }


  def receive = {

    /** Processes each of the downloaded content, transforms it, and sends it to the controller **/
    case body: String =>
      log.info(s"{} Successfully Retrieved url content for $iata.", self)
      context.parent ! Data(toJsonAndTransform(body))
      context.parent ! Done
      context.stop(self)

    /** Failure Cases **/
    case Failed => stop()
    case _: Status.Failure => stop()
  }
}
