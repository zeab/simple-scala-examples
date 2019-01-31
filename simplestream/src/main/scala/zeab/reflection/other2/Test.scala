package zeab.reflection.other2

case class Test(testCaseName:String)(result: => Boolean){
  def execute: TestResult = TestResult(testCaseName, result)
}