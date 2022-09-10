package sample.producer.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import sample.common.service.ClusterMetadata
import sample.common.service.MetadataClient
import sample.producer.model.Message
import sample.producer.model.MessageAck
import java.time.Duration

@Service
class MessageHandler(private val metadataClient: MetadataClient) {
    fun handleMessage(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<Message>().flatMap { m ->
            LOGGER.info("Handling: {}", m)
            val httpHeaders: Map<String, List<String>> = req.headers().asHttpHeaders()
            Mono
                .fromCallable { MessageAck(id = m.id, received = m.payload, headers = httpHeaders) }
                .delayElement(Duration.ofMillis(m.delay))
                .flatMap { messageAck ->
                    metadataClient.getClusterInformation()
                        .switchIfEmpty(Mono.just(ClusterMetadata("", "", "")))
                        .flatMap { metadata ->
                            val withMetadata = messageAck.copy(metadata = metadata)
                            ServerResponse
                                .status(m.responseCode)
                                .body(fromValue(withMetadata))
                        }
                }
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MessageHandler::class.java)
    }
}
