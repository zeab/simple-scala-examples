package zeab.reflect

trait TestCases {

  def test(name: => (String, Boolean)): (String, Boolean) = (name._1, false)

  def test1(name:String)(result: Boolean)(combo: => (String, Boolean) = (name, result)): Unit = Unit

}
