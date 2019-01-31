package zeab.reflect

import scala.reflect.runtime.{universe => ru}

object Reflect {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflect.MyTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val gg = m.classSymbol(instance.getClass)

    val rr = gg.info.decls//.find(m => m.isMethod)


    val ff = instance
      .getClass
      .getDeclaredMethods
      .filter(_.getName.contains("$anon"))
      .map{ma =>

        val yy = m.classSymbol(ma.getClass)
        val gg = ma.getGenericParameterTypes
        ma
      }
//      .map { _.invoke(new Test(""), "b", false) }

    ff.foreach(println)

    println()

  }

}
