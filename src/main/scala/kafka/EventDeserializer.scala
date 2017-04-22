package kafka

import java.util

import kafka.KafkaEvents._
import org.apache.kafka.common.serialization.Deserializer
import play.api.libs.json.Json

/**
  * Created by faiaz on 22.04.17.
  */
// TODO: fix it
class EventDeserializer extends Deserializer[Option[Events]] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def close(): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): Option[Events] = {
    val strMsg = new String(data, "UTF-8")
    val js = Json.parse(strMsg)

    (js \ "name").as[String] match {
      case "hi" => Some(HelloEvent("hi"))
    }
  }
}
