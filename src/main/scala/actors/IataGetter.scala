package actors

import akka.actor.{ActorLogging, Actor}

object IataGetter {
  case class Get()
  case class Failed()
  case class Done()
}


class IataGetter(iata: String) extends Actor with ActorLogging {
  import IataGetter._

  def receive = {

    case Get =>
      log.info("{} started", self)
      println("worker functionality")



  }
}
