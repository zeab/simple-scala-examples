package zeab.reflect

case class Test(name:String, result:Boolean = false){
  def execute(test: Boolean): Test = Test(name, test)
  def llama(t: => Test = Test(this.name, this.result)) = Unit
}
