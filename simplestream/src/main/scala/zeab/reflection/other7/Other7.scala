package zeab.reflection.other7

import scala.reflect.runtime.{universe => ru}

object Other7 {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflection.other7.MyTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val ff = instance
      .getClass
      .getDeclaredMethods
      .filter(_.getName.contains("$anon"))
      .map { _.invoke(null) }

    ff.foreach(println)

    println()
  }

}
