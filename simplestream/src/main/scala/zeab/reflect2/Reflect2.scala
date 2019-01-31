package zeab.reflect2

import akka.http.scaladsl.server.util.Tuple

import scala.reflect.runtime.{universe => ru}

object Reflect2 {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflect2.MyTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val rr = instance
      .getClass
      .getDeclaredMethods

    val ff = instance
      .getClass
      .getDeclaredMethods
      .filter(_.getName.contains("$anon"))
      .map { _.invoke(null) }

    ff.foreach(println)

    println()

  }

}
