package zeab.reflection.other7

class Core(n:String)(r:Boolean)

object Core{
  def apply(n: String)(r: Boolean): TestResult = TestResult(n, r)
}
