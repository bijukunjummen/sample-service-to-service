package sample.producer.model

data class MessageAck(val id: String?, val received: String, val headers: Map<String, List<String>>)