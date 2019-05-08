package simplecassandrawriter

//Imports
import com.datastax.driver.core.{Cluster, Session}
import zeab.randomness.ThreadLocalRandom
import zeab.sys.EnvironmentVariables

import scala.collection.JavaConverters._

//Start a local cassandra instance
//docker run --name my-dse -d --network=host -e DS_LICENSE=accept datastax/dse-server

object SimpleCassandraWriter extends App with CassLogging with TableInit with ThreadLocalRandom with EnvironmentVariables {

  val cluster: Cluster =
    Cluster.builder
      .addContactPoint(getEnvVar[String]("IP"))
      .withPort(getEnvVar[Int]("PORT"))
      .withCredentials(getEnvVar[String]("USER"), getEnvVar[String]("PASS"))
      .build
  val session: Session = cluster.connect()

  session.execute(createKeyspace)
  session.execute(createTable)
  for (i <- 1 to 1000){
    session.execute(insertTable(i, getRandomAlpha(5)))
  }

  //How to grab it if you dont know the id's
  val rows = session.execute(selectFromTable).asScala.toStream
  val x = rows.find(_.getInt(0) == 103)

  //how to grab it if you do know
  val y = session.execute(selectFromTable(103)).asScala.headOption
  rows.foreach(println)

  session.close()
  cluster.close()

}