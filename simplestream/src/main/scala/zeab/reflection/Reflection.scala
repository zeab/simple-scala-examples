package zeab.reflection

import scala.reflect.runtime.{universe => ru}


object Reflection {

  case class Person(name: String){
    def sayMyName(): Unit = println(name)
  }

  case class TestResult(testName:String, testResult:Boolean)

  trait TestTest {
    case class Test(testName:String)(testResult: => Boolean){
      def execute = TestResult(testName, testResult)
    }
  }

  class MyTest extends TestTest {
    Test("my test"){
      println("hello")
      true
    }
    Test("my 2nd test"){
      println("hello2")
      false
    }
    Test("my test11"){
      println("hello")
      true
    }
    Test("my 2nd test22"){
      println("hello2")
      false
    }
    Test("my test111"){
      println("hello")
      true
    }
    Test("my 2nd test222"){
      println("hello2")
      false
    }
  }

  def main(args: Array[String]): Unit = {

    val q = new MyTest

    //Basic
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflection.Reflection.MyTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val ss = instance.getClass.getDeclaredMethods

    println(instance.asInstanceOf[TestTest])

//    val p = Person("panda")
//    val methodName = "sayMyName"
//    val reflected = m.reflect(p)
//    val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
//    val method = reflected.reflectMethod(methodSymbol)
//    method.apply()


    val testReflected = m.reflect(q)
    val methods = testReflected.getClass.getDeclaredMethods

    println()

    //Methods
//    val methodName = "execute"
//    val reflected = m.reflect(t)
//    val methodSymbol = ru.typeOf[Test].decl(ru.TermName(methodName)).asMethod
//    val method = reflected.reflectMethod(methodSymbol)
//    val ss = method.apply()

//    println(ss)
    //Tag Types
    import ru._

    val ttag = typeTag[Person]
    println(ttag.tpe)




  }

}
