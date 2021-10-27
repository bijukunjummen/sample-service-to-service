package sample.caller.service

import org.springframework.http.HttpHeaders
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck

interface MessageHandler {
    fun handle(message: Message, callerHeaders: HttpHeaders): Mono<MessageAck>
}