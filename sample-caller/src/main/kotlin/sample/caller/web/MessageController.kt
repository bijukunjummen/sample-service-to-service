package sample.caller.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import sample.caller.service.MessageHandler
import sample.common.service.ClusterMetadata
import sample.common.service.MetadataClient

@RestController
class MessageController(private val messageHandler: MessageHandler, private val metadataClient: MetadataClient) {
    @PostMapping("/caller/messages")
    fun handle(@RequestBody message: Message, @RequestHeader callerHeaders: HttpHeaders): Mono<MessageAck> {
        LOGGER.info("Handling message {}", message)
        return messageHandler.handle(message, callerHeaders)
            .flatMap { messageAck ->
                metadataClient.getClusterInformation()
                    .switchIfEmpty(Mono.just(ClusterMetadata("", "", "")))
                    .map { clusterData ->
                        messageAck.copy(callerMetadata = clusterData)
                    }
            }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MessageController::class.java)
    }
}
