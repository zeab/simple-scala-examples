package zeab.reflection.other

trait HalTests {

  def test(testCaseName:String)(result: => Boolean)(testResult: => (String, Boolean) = (testCaseName, result)) ={}

  def add(x: Int)(y: Int): Int = {
    x + y
  }
//  def add1(x: String): (Int => Int) = {
//    (y: Int) => {
//      x + y
//    }
//  }

}
