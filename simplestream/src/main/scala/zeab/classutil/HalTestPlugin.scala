package zeab.classutil

trait HalTestPlugin {

  def test(testName:String)(result: => Boolean)(combo: => (String, Boolean) = (testName, result)): Unit = Unit

}
