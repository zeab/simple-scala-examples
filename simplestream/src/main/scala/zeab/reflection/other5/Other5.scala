package zeab.reflection.other5

import zeab.reflection.other2.Other2.getClass

import scala.reflect.runtime.{universe => ru}

object Other5 {

  def main(args: Array[String]): Unit = {

    val testCase = "zeab.reflection.other5.MyTest"
    val test = ClassLoader.getSystemClassLoader.loadClass(testCase).getDeclaredConstructor().newInstance().asInstanceOf[Suites]


    val rr = test.getClass.getDeclaredMethods

    val m = ru.runtimeMirror(getClass.getClassLoader)

    val methodName = "execute"
    val reflected = m.reflect(test)
    val methodSymbol = ru.typeOf[Test].decl(ru.TermName(methodName)).asMethod

    val method = reflected.reflectMethod(methodSymbol)
    val ss = method.apply()



    println(test)

  }

}
