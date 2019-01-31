package zeab.reflection.other4

import scala.reflect.runtime.{universe => ru}

object Other4 {

  case class PersonPlus(name:String, alive: Boolean)

  case class Person(name: String)(result: => Boolean){
    def execute = PersonPlus(name, result)
  }

  def main(args: Array[String]): Unit = {

    val m = ru.runtimeMirror(getClass.getClassLoader)
    val classPerson = ru.typeOf[Person].typeSymbol.asClass
    val cm = m.reflectClass(classPerson)
    val ctor = ru.typeOf[Person].decl(ru.termNames.CONSTRUCTOR).asMethod
    val ctorm = cm.reflectConstructor(ctor)


    val p = ctorm("Mike", true).asInstanceOf[Person].execute


    println(p)

  }

}
