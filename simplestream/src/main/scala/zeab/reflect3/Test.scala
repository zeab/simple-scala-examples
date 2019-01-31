package zeab.reflect3

case class Test(name:String = "", result: Boolean = false){
  def test(test:Boolean) = copy(name, test)
  def execute(r: => (String, Boolean) = (this.name, this.result) ) = Unit
}
