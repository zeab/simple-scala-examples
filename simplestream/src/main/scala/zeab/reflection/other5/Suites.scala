package zeab.reflection.other5

trait Suites {

  def test(testCaseName:String)(testResult: => Boolean)(tr: TestResult = TestResult(testCaseName, testResult))

}
