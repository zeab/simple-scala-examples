package zeab.reflection.testasfunc

trait TestCases {

  def test(testCaseName:String)(result: Boolean): TestResult = TestResult(testCaseName, result)

}
