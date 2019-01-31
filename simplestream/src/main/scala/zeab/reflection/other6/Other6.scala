package zeab.reflection.other6

import scala.reflect.runtime.{universe => ru}

object Other6 {

  def main(args: Array[String]): Unit = {

    object X {
      def aa = 1
      def ab = 2
      def removeAtX = 3
      def bb = 4
      def removeAtY = 5
    }

    val ru = scala.reflect.runtime.universe
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val im = m.reflect(X)
    val l = X.getClass.getMethods.map(_.getName).filter(_ startsWith "removeAt")
    val r = l.map(y => ru.typeOf[X.type].declaration(ru.newTermName(y)).asMethod).map(im.reflectMethod(_).apply())


  }

}
