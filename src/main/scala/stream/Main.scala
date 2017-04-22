package stream

import com.typesafe.config.ConfigFactory
import kafka.EventDeserializer
import kafka.KafkaEvents.Events
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.JavaConversions._
import scala.reflect._

/**
  * Created by faiaz on 22.04.17.
  */
object Main extends App {

  val conf = ConfigFactory.load("app.conf")
  val brokerList = conf.getString("kafka.brokerList")
  val topicsSet = conf.getStringList("kafka.topic").toSet
  val appName = conf.getString("spark.appName")
  val master = conf.getString("spark.master")

  val sparkConf = new SparkConf()
    .setAppName(appName)
    .setMaster(master)

  val ssc = new StreamingContext(sparkConf, Seconds(1))
  ssc.sparkContext.setLogLevel("ERROR")

  val kafkaParams = Map[String, String]("metadata.broker.list" -> brokerList)

  val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    ssc, kafkaParams, topicsSet)

  stream.map(_._2).print()

  ssc.start()
  ssc.awaitTermination()
}
