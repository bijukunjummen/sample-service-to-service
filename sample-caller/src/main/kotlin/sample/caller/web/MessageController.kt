package sample.caller.web

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import sample.caller.service.MessageHandler

@RestController
class MessageController(private val messageHandler: MessageHandler) {

    @PostMapping("/messages")
    fun handle(@RequestBody message: Message): Mono<MessageAck> {
        return messageHandler.handle(message)
    }
}