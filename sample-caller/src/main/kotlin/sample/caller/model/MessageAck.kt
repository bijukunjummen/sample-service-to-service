package sample.caller.model

data class MessageAckLite(val id: String?, val received: String, val ack: String)
data class MessageAck(val id: String?, val received: String, val ack: String, val statusCode: Int, val roundTripTimeMillis: Long)