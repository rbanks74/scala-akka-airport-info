package services

import actors.{IataGetter, IataController}
import akka.actor.Props


/** Create overridable factory methods to stub out creation and behavior of IataController and IataGetter Actor for testing **/
object IataProps {

  def iataControllerProps: Props = Props(new IataController)
  def iataGetterProps: Props = Props(new IataGetter)

}
