//package zeab.reflection.other3
//
//import scala.reflect.ClassTag
//
//import scala.reflect.runtime.{universe => ru}
//
//object Other3 {
//
//  def main(args: Array[String]): Unit = {
//
//
//    class A {
//      def process = {
//        (1 to 1000).foreach(x => x + 10)
//        println("ok!")
//      }
//    }
//
//    def getTypeTag[T: ru.TypeTag](obj: T) = ru.typeTag[T]
//
//    def perf[T : ClassTag : ru.TypeTag](t: T, sMethodName: String): Any = {
//      val m = ru.runtimeMirror(t.getClass.getClassLoader)
//      val myType = ru.typeTag[T].tpe
//      val mn = myType.decl(ru.TermName(sMethodName)).asMethod
//      val im = m.reflect(t)
//      val toCall = im.reflectMethod(mn)
//      toCall()
//    }
//
//
//    val a = new A
//    perf(a, "process")
//
//
//  }
//
//}
