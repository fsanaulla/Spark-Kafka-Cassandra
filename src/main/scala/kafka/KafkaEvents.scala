package kafka

import play.api.libs.json.Json

/**
  * Created by faiaz on 22.04.17.
  */
object KafkaEvents {

  sealed trait Events {
    val name: String
  }

  case class HelloEvent(override val name: String) extends Events
  object HelloEvent {
    implicit val fmt = Json.format[HelloEvent]
  }
}
