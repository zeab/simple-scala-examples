package zeab.reflection.other2

class Test2(testCaseName:String)(result: => Boolean){}

object Test2{
  def apply(testCaseName:String)(result: => Boolean): TestResult = TestResult(testCaseName, result)
}
