package sample.producer.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import sample.producer.model.Message
import sample.producer.model.MessageAck
import java.time.Duration

@Service
class MessageHandler {

    private val logger: Logger = LoggerFactory.getLogger(MessageHandler::class.java)

    fun handleMessage(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<Message>().flatMap { m ->
            logger.info("Handling message: {}", m)
            Mono
                .fromCallable { MessageAck(id = m.id, received = m.payload, ack = "ack") }
                .delayElement(Duration.ofMillis(m.delay))
                .flatMap { messageAck ->
                    if (m.throwException) {
                        Mono.error(RuntimeException("Something went wrong!!"))
                    } else {
                        ServerResponse
                            .status(HttpStatus.OK)
                            .body(fromValue(messageAck))
                    }
                }
        }
    }

}