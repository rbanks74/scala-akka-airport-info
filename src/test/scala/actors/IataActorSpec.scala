package actors



object IataActorSpec {
  val iataCode = "IAD"

  val htmlResult = "{\"delay\":\"false\",\"IATA\":\"IAD\",\"state\":\"District of Columbia\",\"name\":\"Washington Dulles International\",\"weather\":{\"visibility\":2.00,\"weather\":\"Rain Fog/Mist\",\"meta\":{\"credit\":\"NOAA's National Weather Service\",\"updated\":\"8:52 AM Local\",\"url\":\"http://weather.gov/\"},\"temp\":\"38.0 F (3.3 C)\",\"wind\":\"North at 8.1mph\"},\"ICAO\":\"KIAD\",\"city\":\"Washington\",\"status\":{\"reason\":\"No known delays for this airport.\",\"closureBegin\":\"\",\"endTime\":\"\",\"minDelay\":\"\",\"avgDelay\":\"\",\"maxDelay\":\"\",\"closureEnd\":\"\",\"trend\":\"\",\"type\":\"\"}} "

  val result = Map(iataCode -> htmlResult)
/**
  object FakeDownloadClient extends DownloadClient {
    def downloadPage(code: String): Future[String] = result get code match {
      case None => throw new Exception(s"Error: Could not find $code.")
      case Some(downloadString) => Future.successful(downloadString)
    }
  }
**/
/**
  def fakeIataGetter(iata: String): Props = {
    Props(new IataGetter(iata) {
      override val downloadClient = FakeDownloadClient.downloadPage(iata)
      println(downloadClient.foreach(println(_)))
    })
  }
}


class StepParent(child: Props, probe: ActorRef) extends Actor {

  println("inside step parent")
  context.actorOf(child, "child")
  println(child)
  def receive = {
    case msg => probe.tell(msg, sender())
  }
}
**/
/** TODO: Fix Test
class IataGetterSpec extends TestKit(ActorSystem("IataGetterSpec")) with WordSpecLike with BeforeAndAfterAll with ImplicitSender {
  import IataGetterSpec._

  override def afterAll() = system.shutdown()

  "A Getter" must {
    "return the right body" in {
      val getter = system.actorOf(Props(new StepParent(fakeIataGetter(iataCode), testActor)), "rightBody")
      expectMsg(IataController.Data(toJsonAndTransform(htmlResult)))
      expectMsg(IataGetter.Done)
    }
  }
  **/
}
