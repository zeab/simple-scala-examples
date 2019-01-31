package zeab.reflection.testasdsl

import scala.reflect.runtime.{universe => ru}

object TestAsDsl {

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflection.testasdsl.MyTest")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()

    val aa = instance.getClass.getAnnotations
    val bb = instance.getClass.getAnnotatedInterfaces
    val cc = instance.getClass.getClasses
    val dd = instance.getClass.getFields
    val ee = instance.getClass.getMethods
    val ff = instance.getClass.getInterfaces
    val gg = instance.getClass.getTypeParameters
    val hh = instance.getClass.getConstructor()
    val ii = instance.getClass.getSigners

    println(instance)

  }

}
