import actors.IataReceiver
import akka.actor.{ActorSystem, Props}

/** Program to retrieve the airport statuses from the FAA web feed **/
object Main extends App {

  val iataSystem = ActorSystem("IATA")
  val iataReceiver = iataSystem.actorOf(Props(new IataReceiver), "IataReceiver")

}
