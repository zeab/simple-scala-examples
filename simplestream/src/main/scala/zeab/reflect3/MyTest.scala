package zeab.reflect3

class MyTest {

  Test("llama").test{
    println("1")
    1 + 1 == 2
  }.execute()

  Test("llam1a").test{
    println("2")
    1 + 1 == 3
  }.execute()

}
