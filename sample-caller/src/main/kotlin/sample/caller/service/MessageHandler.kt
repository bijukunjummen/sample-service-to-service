package sample.caller.service

import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck

interface MessageHandler {
    fun handle(message: Message): Mono<MessageAck>
}