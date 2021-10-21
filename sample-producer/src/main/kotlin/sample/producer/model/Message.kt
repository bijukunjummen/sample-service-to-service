package sample.producer.model

import org.springframework.http.HttpStatus
import java.util.UUID

data class Message(
        val id: String = UUID.randomUUID().toString(),
        val payload: String,
        val delay: Long,
        val responseCode: Int = HttpStatus.OK.value()
)