package kafka

/**
  * Created by faiaz on 22.04.17.
  */
object KafkaEvents {

  sealed trait Events {
    val message: String
  }

  case class HelloEvent(message: String) extends Events
}
