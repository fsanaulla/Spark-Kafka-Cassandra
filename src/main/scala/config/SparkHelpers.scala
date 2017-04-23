package config

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scala.collection.JavaConversions._

/**
  * Created by faiaz on 23.04.17.
  */
trait SparkHelpers {

  //CONFIGURATION
  val conf = ConfigFactory.load("application.conf")

  //SYSTEM
  val logLevel = conf.getString("system.logLevel")

  //KAFKA
  val brokerList = conf.getString("kafka.brokerList")
  val topicsSet = conf.getStringList("kafka.topic").toSet

  //SPARK
  val appName = conf.getString("spark.appName")
  val master = conf.getString("spark.master")

  //CASSANDRA
  val cassandraHost = conf.getString("cassandra.host")
  val cassandraPort = conf.getInt("cassandra.port").toString
  val cassandraUser = conf.getString("cassandra.user")
  val cassandraPassword = conf.getString("cassandra.password")


  val sparkConf = new SparkConf()
    .setAppName(appName)
    .setMaster(master)
    .set("spark.cassandra.connection.host", cassandraHost)
    .set("spark.cassandra.connection.port", cassandraPort)
    .set("spark.cassandra.auth.username", cassandraUser)
    .set("spark.cassandra.auth.password", cassandraPassword)

  val ssc = new StreamingContext(sparkConf, Seconds(1))

  ssc.sparkContext.setLogLevel(logLevel)
}
