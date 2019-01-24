package zeab.reflection

import scala.reflect.runtime.{universe  => ru}

object Reflection {

  case class Person(name: String){
    def sayMyName(): Unit = println(name)
  }

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass("zeab.reflection.Reflection.Person")
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply("Llama")

    println(instance.asInstanceOf[Person].name)

    val p = Person("panda")
    val methodName = "sayMyName"
    val reflected = m.reflect(p)
    val methodSymbol = ru.typeOf[Person].decl(ru.TermName(methodName)).asMethod
    val method = reflected.reflectMethod(methodSymbol)
    method.apply()
    
  }

}
