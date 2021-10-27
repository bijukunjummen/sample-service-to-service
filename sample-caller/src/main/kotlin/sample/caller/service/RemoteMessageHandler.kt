package sample.caller.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.StopWatch
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import sample.caller.model.MessageAckLite
import java.net.URI

@Service
class RemoteMessageHandler(
        private val webClientBuilder: WebClient.Builder,
        @Value("\${remote.base.url}") private val remoteBaseUrl: String
) : MessageHandler {
    override fun handle(message: Message, callerHeaders: HttpHeaders): Mono<MessageAck> {
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
                    Mono.deferContextual { contextView ->
                        val stopWatch = contextView.get<StopWatch>(STOPWATCH_KEY)
                        stopWatch.stop()
                        val roundTripTime = stopWatch.totalTimeMillis

                        if (response.statusCode().is2xxSuccessful) {
                            response
                                    .bodyToMono<MessageAckLite>()
                                    .map { lite ->
                                        MessageAck(id = lite.id,
                                                received = lite.received,
                                                statusCode = response.rawStatusCode(),
                                                producerHeaders = lite.headers,
                                                callerHeaders = callerHeaders,
                                                roundTripTimeMillis = roundTripTime)
                                    }
                        } else {
                            response
                                    .bodyToMono<String>()
                                    .map { responseAsString ->
                                        MessageAck(id = message.id,
                                                received = "Raw Failed Message from Producer: $responseAsString",
                                                producerHeaders = emptyMap(),
                                                callerHeaders = callerHeaders,
                                                statusCode = response.rawStatusCode(),
                                                roundTripTimeMillis = roundTripTime)
                                    }
                        }
                    }
                }.contextWrite { context ->
                    val stopWatch = StopWatch()
                    stopWatch.start()
                    context.put(STOPWATCH_KEY, stopWatch)
                }
    }

    companion object {
        private const val STOPWATCH_KEY = "stopWatch"
    }
}