package zeab.reflect2

class MyTest {

  Test("test 1").execute{
    println("test 1")
    1 + 1 == 2
  }.output()

  Test("test 2").execute{
    println("test 2")
    1 + 1 == 3
  }.output()

  Test("test 3").execute{
    println("test 3")
    1 + 1 == 2
  }.output()

}
