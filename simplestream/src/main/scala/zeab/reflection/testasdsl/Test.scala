package zeab.reflection.testasdsl

case class Test(testName:String){
  def toTest(test:Boolean) = TestResult(testName, test)
}

