package zeab.reflect2

class Test(name:String = "", result:Boolean = false) {
  def execute(test:Boolean): Test = new Test(name, test)
  def output(res: => (String, Boolean) = (this.name, this.result)): (String, Boolean) = (this.name, this.result)
}

object Test{
  def apply(name: String): Test = new Test(name, false)
  def apply(name: String, result: Boolean = false): (String, Boolean) = (name, result)
}