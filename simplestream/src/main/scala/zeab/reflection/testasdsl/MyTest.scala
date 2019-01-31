package zeab.reflection.testasdsl

class MyTest {

  Test("test 1").toTest{
    println("test 1")
    1 + 1 == 2
  }

  Test("test 2").toTest{
    println("test 2")
    1 + 1 == 3
  }

  Test("test 3").toTest{
    println("test 3")
    1 + 1 == 2
  }

}

