package zeab.reflection.other5

case class Test(testCaseName:String)(result: => Boolean){
  def execute: TestResult = TestResult(testCaseName, result)
}
