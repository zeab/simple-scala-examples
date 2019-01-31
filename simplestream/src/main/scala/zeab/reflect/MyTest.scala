package zeab.reflect

class MyTest extends TestCases {

  Test("llama").execute{
    println("llama")
    1 + 1 == 2
  }.llama()

  Test("panda").execute{
    println("panda")
    1 + 1 == 3
  }.llama()

//  test(("my test 1", 1 + 1 == 2))
//
//
//  test1("test 1"){
//    println("test 1")
//    1 + 1 == 2
//  }()

//
//  test{("my test 2",
//    1 + 1 == 3
//  )}
//
//  test{("my test 3",
//    1 + 1 < 10
//  )}

}
