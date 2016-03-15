package actors

import actors.IataGetter.{Failed, Process}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import services.IataProps.iataGetterProps

class IataGetterSpec extends TestKit(ActorSystem("IataGetterSpec")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "An IataGetter" should {
    "send the Data and a Done response to the IataController" in {
      val proxy = TestProbe()
      val parent: ActorRef = system.actorOf(Props(new Actor {
        val child = context.actorOf(iataGetterProps, "child")

        def receive = {
          case x if sender() == child     => proxy.ref forward x
          case x                          => child forward x
        }
      }))

      proxy.send(parent, Process("IAD"))
      proxy.expectMsgClass(classOf[IataController.Data])
      proxy.expectMsg(IataGetter.Done)
    }
  }

  "An IataGetter" should {
    "stop the actor when the incorrect Iata Code is received" in {

      val proxy = TestProbe()
      val parent: ActorRef = system.actorOf(Props(new Actor {
        val child = context.actorOf(iataGetterProps, "child")

        def receive = {
          case x if sender() == child     => proxy.ref forward x
          case x                          => child forward x
        }
      }))

      proxy.send(parent, Process("123"))
      proxy.expectMsg(IataGetter.Done)
    }
  }

  "An IataGetter" should {
    "stop the actor when Failed message is Received" in {

      val proxy = TestProbe()
      val parent: ActorRef = system.actorOf(Props(new Actor {
        val child = context.actorOf(iataGetterProps, "child")

        def receive = {
          case x if sender() == child     => proxy.ref forward x
          case x                          => child forward x
        }
      }))

      proxy.send(parent, Failed)
      proxy.expectMsg(IataGetter.Done)
    }
  }

  "An IataGetter" should {
    "stop the actor when an unknown message is Received" in {

      val proxy = TestProbe()
      val parent: ActorRef = system.actorOf(Props(new Actor {
        val child = context.actorOf(iataGetterProps, "child")

        def receive = {
          case x if sender() == child     => proxy.ref forward x
          case x                          => child forward x
        }
      }))

      proxy.send(parent, IataController.Done)
      proxy.expectMsg(IataGetter.Done)
    }
  }
}
