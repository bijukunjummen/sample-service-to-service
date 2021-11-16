package sample.caller.model

import sample.common.service.ClusterMetadata

data class MessageAckLite(
        val id: String?,
        val received: String,
        val headers: Map<String, List<String>>,
        val metadata: ClusterMetadata?
)

data class MessageAck(
        val id: String?,
        val received: String,
        val callerHeaders: Map<String, List<String>>,
        val producerHeaders: Map<String, List<String>>,
        val callerMetadata: ClusterMetadata? = null,
        val producerMetadata: ClusterMetadata? = null,
        val statusCode: Int,
        val roundTripTimeMillis: Long)