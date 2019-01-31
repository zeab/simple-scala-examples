package zeab.classutil

import java.io.File
import reflect.runtime.universe._

import org.clapper.classutil.ClassFinder

object ClassUtilExamples {

  def main(args: Array[String]): Unit = {

    def getType[T: TypeTag](obj: T) = typeOf[T]

    val classpath = List(".").map(new File(_))
    val finder = ClassFinder(classpath)
    val classes = finder.getClasses
    val classMap = ClassFinder.classInfoMap(classes)
    val plugins = ClassFinder.concreteSubclasses("zeab.classutil.HalTestPlugin", classMap).map{plugin =>
      Class.forName(plugin.name).newInstance().asInstanceOf[HalTestPlugin]
    }.toList

    val plugins1 = ClassFinder.concreteSubclasses("zeab.classutil.HalTestPlugin", classMap).map{plugin =>
      Class.forName(plugin.name).newInstance().asInstanceOf[HalTestPlugin].getClass.getMethods
    }.toList

    val kk = plugins.head.getClass.getMethods
      .filter(_.getReturnType == classOf[(String, Boolean)])
      .filter(_.getName.contains("$anon"))

    kk.map{a => plugins.head.getClass.getMethod(a.getName, null)}


    plugins.map{t =>
      val f = t.getClass.getMethods
      .filter(_.getReturnType == classOf[(String, Boolean)])
      .filter(_.getName.contains("$anon"))


    }

      //.map{a => a.invoke(null, null)}
//      .map{m =>
//        val g = m
//        val s = g.getDefaultValue
//        m
//      }

    kk.foreach(println)


    println()
  }

}
