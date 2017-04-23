package stream

import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector.streaming._
import config.SparkHelpers
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka._
/**
  * Created by faiaz on 22.04.17.
  */
object Main extends App with SparkHelpers {

  val kafkaParams = Map[String, String]("metadata.broker.list" -> brokerList)

  val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    ssc, kafkaParams, topicsSet)

  stream
    .map(_._2)
    .map(str => str -> 1)
    .saveToCassandra("dev", "kv", SomeColumns("key", "value"))

  ssc.start()
  ssc.awaitTermination()
}
