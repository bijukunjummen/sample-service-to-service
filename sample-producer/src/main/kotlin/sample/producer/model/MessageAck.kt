package sample.producer.model

data class MessageAck(val id: String?, val received: String, val ack: String)