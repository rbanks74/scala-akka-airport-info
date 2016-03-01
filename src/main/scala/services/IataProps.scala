package services

import actors.{IataGetter, IataController}
import akka.actor.Props


/** Create overridable factory methods to stub out creation and behavior of IataController and IataGetter Actor for testing **/
object IataProps {

  def iataControllerProps(stringList: List[String]): Props = Props(new IataController(stringList))
  def iataGetterProps(iataCode: String): Props = Props(new IataGetter(iataCode))

}
