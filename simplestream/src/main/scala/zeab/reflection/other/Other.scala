package zeab.reflection.other

import scala.reflect.runtime.{universe => ru}
import scala.util.Try

object Other {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflection.other.MyTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val ff = instance.getClass.getMethods
      .filter(_.getReturnType == classOf[(String, Boolean)])
      .filter(_.getName.contains("$anon"))
      //.map(test => test.invoke(Unit))

//    val gg = instance.getClass.getMethods.map{a =>
//      println(a.getAnnotatedReturnType)
//    }

    println("")

  }

}

//scala.Tuple2