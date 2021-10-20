package sample.caller.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import java.net.URI

@Service
class RemoteMessageHandler(
    private val webClientBuilder: WebClient.Builder,
    @Value("\${remote.base.url}") private val remoteBaseUrl: String
) : MessageHandler {
    override fun handle(message: Message): Mono<MessageAck> {
        val webClient: WebClient = webClientBuilder.build()
        val uri: URI = UriComponentsBuilder
            .fromHttpUrl("$remoteBaseUrl/producer/messages")
            .build()
            .toUri()
        return webClient.post()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(message))
            .exchangeToMono { response ->
                response.bodyToMono<MessageAck>()
            }
    }
}