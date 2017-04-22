package stream

import com.typesafe.config.ConfigFactory
import kafka.KafkaEvents.HelloEvent
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import play.api.libs.json.Json
import com.datastax.spark.connector.streaming._


import scala.collection.JavaConversions._

/**
  * Created by faiaz on 22.04.17.
  */
object Main extends App {

  //CONFIGURATION
  val conf = ConfigFactory.load("app.conf")
  val brokerList = conf.getString("kafka.brokerList")
  val topicsSet = conf.getStringList("kafka.topic").toSet
  val logLevel = conf.getString("system.logLevel")
  val appName = conf.getString("spark.appName")
  val master = conf.getString("spark.master")

  val sparkConf = new SparkConf()
    .setAppName(appName)
    .setMaster(master)

  val ssc = new StreamingContext(sparkConf, Seconds(1))
  ssc.sparkContext.setLogLevel(logLevel)

  val kafkaParams = Map[String, String]("metadata.broker.list" -> brokerList)

  //STREAM
  val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    ssc, kafkaParams, topicsSet)

  stream.map(_._2)
    .map(str => Json.parse(str).as[HelloEvent])
//    .saveToCassandra()

  ssc.start()
  ssc.awaitTermination()
}
