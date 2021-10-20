package sample.caller.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import sample.caller.service.MessageHandler

@RestController
class MessageController(private val messageHandler: MessageHandler) {
    @PostMapping("/caller/messages")
    fun handle(@RequestBody message: Message): Mono<MessageAck> {
        LOGGER.info("Handling message {}", message)
        return messageHandler.handle(message)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MessageController::class.java)
    }
}