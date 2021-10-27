package sample.caller.model

data class MessageAckLite(
        val id: String?,
        val received: String,
        val headers: Map<String, List<String>>)

data class MessageAck(
        val id: String?,
        val received: String,
        val callerHeaders: Map<String, List<String>>,
        val producerHeaders: Map<String, List<String>>,
        val statusCode: Int,
        val roundTripTimeMillis: Long)