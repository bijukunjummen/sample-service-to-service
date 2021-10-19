package sample.producer.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val payload: String,
    val delay: Long,
    val throwException: Boolean = false
)