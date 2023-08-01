package sample.producer.model

import sample.common.service.ClusterMetadata

data class MessageAck(
    val id: String?,
    val received: String,
    val headers: Map<String, List<String>>,
    val metadata: ClusterMetadata? = null
)