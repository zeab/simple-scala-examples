package simplecassandrawriter

import ch.qos.logback.classic.Level
import zeab.logging.Logging

trait CassLogging extends Logging {

  val cassandraLogLevel: Level = getLogLevel("CASSANDRA_LOG_LEVEL", "WARN")
  loggerContext.getLogger("com.datastax.driver.core").setLevel(cassandraLogLevel)
  loggerContext.getLogger("io.netty.util.internal").setLevel(cassandraLogLevel)


}
