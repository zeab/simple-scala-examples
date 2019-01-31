package zeab.reflection.other

class MyTest extends HalTests {

  add(8)(7)

  test("test 1"){
    println("test 1")
    true
  }()

  test("test 2"){
    println("test 2")
    false
  }()

  test("test 3"){
    println("test 3")
    true
  }()

}
