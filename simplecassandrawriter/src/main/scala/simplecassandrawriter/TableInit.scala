package simplecassandrawriter

trait TableInit {

  val createKeyspace: String ={
      """CREATE KEYSPACE IF NOT EXISTS emp
      |  WITH REPLICATION = {
      |   'class' : 'SimpleStrategy',
      |   'replication_factor' : 1
      |  };""".stripMargin
  }

  val createTable: String ={
      """CREATE TABLE IF NOT EXISTS emp.emp(
      |   emp_id int PRIMARY KEY,
      |   emp_name text
      |   );""".stripMargin
  }

  def insertTable(id:Int, name:String): String ={
    s"""INSERT INTO emp.emp (emp_id, emp_name)
    | VALUES ($id, '$name');""".stripMargin
  }

  val selectFromTable: String = "select * from emp.emp;"

  def selectFromTable(id:Int): String = s"select * from emp.emp WHERE emp_id = $id;"

}
