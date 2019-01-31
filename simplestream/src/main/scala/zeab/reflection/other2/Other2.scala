package zeab.reflection.other2

import zeab.reflection.Reflection

import scala.reflect.runtime.{universe => ru}

object Other2 {

  class ExtendedTest {
    type Y = String
    type Test = TestResult
    val y: Y = "y"
    def test(testCaseName:String)(result: => Boolean) = TestResult(testCaseName, result)
  }

//  def m1(x: => Int): List[Int] = {
//    val y=x
//    List(y, y)
//  }

  def main(args: Array[String]): Unit = {






    //Dont even know what to do with this information
    def m4(x: Int) = 4*x
    val f4 = m4 _

    val m = ru.runtimeMirror(getClass.getClassLoader)

//    val gg = Test("llama"){false}
//
//    val methodName = "execute"
//    val reflected = m.reflect(test)
//    val methodSymbol = ru.typeOf[Test].decl(ru.TermName(methodName)).asMethod
//    val method = reflected.reflectMethod(methodSymbol)
//    val ss = method.apply()

    val clazz = m.staticClass("zeab.reflection.other2.MyFirstTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val gg = instance.getClass.getMethods
    val tt = instance.getClass.getDeclaredAnnotations
    val yy = instance.getClass.getDeclaredClasses
    val uu = instance.getClass.getDeclaringClass



    val ff = instance.getClass.getDeclaredMethods.filter(_.getName.contains("$anon")).map{test =>
//      val methodName = "execute"
//      val reflected = m.reflect(test)
//      val methodSymbol = ru.typeOf[Test].decl(ru.TermName(methodName)).asMethod
//
//      val method = reflected.reflectMethod(methodSymbol)
//      val ss = method.apply()
//      ss

      val ss = test.getDefaultValue
      val tt = test.getAnnotatedReturnType
      val uu = test.getGenericParameterTypes
      val ii = test.getParameterAnnotations
      val oo = test.getTypeParameters
      val ee = test.getReturnType
      test
    }



//    val someTrait:SomeTrait = obj.instance.asInstanceOf[SomeTrait]
//    someTrait.someMethod

//    val rr = instance.getClass.getClasses
//
//
//    println(instance)
//
//
//
//    type User = {
//      val name: String
//      val surname: String
//    }
//
//    def llama(a:User) = a.name

  }

}
