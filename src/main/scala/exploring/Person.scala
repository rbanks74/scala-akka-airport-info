package exploring


case class Man(name: String)
case class Woman(name: String)

object Humanity {
  type Male = Man
  type Female = Woman
  type Age = Int

  val manData: Map[Male, Age] = Map.empty
  val womanData: Map[Female, Age] = Map.empty
}

