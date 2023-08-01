package sample.caller.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import sample.caller.model.Message

class RemoteMessageHandlerTest {

    @Test
    fun `a clean flow`() {
        val clientResponse: ClientResponse = ClientResponse
            .create(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(
                """
                 | {
                 |  "id": "123",
                 |  "received": "test",
                 |  "headers": {},
                 |  "metadata": {"clusterName": "test", "clusterLocation": "us-west1"}
                 | }
                """.trimMargin().trimIndent()
            )
            .build()
        val shortCircuitingExchangeFunction = ExchangeFunction {
            Mono.just(clientResponse)
        }
        val webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction)
        val remoteMessageHandler = RemoteMessageHandler(webClientBuilder, "http://someurl")
        StepVerifier
            .create(
                remoteMessageHandler.handle(
                    Message(id = "id", payload = "test", delay = 1L, responseCode = 200),
                    HttpHeaders()
                )
            )
            .assertNext { ack ->
                assertThat(ack.id).isEqualTo("123")
                assertThat(ack.statusCode).isEqualTo(200)
                assertThat(ack.producerMetadata?.clusterName).isEqualTo("test")
            }
            .verifyComplete()
    }

    @Test
    fun `an error flow`() {
        val clientResponse: ClientResponse = ClientResponse
            .create(HttpStatus.INTERNAL_SERVER_ERROR)
            .header("Content-Type", "application/json")
            .body(
                """
                 | {
                 |  "id": "123",
                 |  "received": "test",
                 |  "headers":{}
                 | }
                """.trimMargin().trimIndent()
            )
            .build()
        val shortCircuitingExchangeFunction = ExchangeFunction {
            Mono.just(clientResponse)
        }
        val webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction)
        val remoteMessageHandler = RemoteMessageHandler(webClientBuilder, "http://someurl")
        StepVerifier
            .create(
                remoteMessageHandler.handle(
                    Message(id = "id", payload = "test", delay = 1L, responseCode = 500),
                    HttpHeaders()
                )
            )
            .assertNext { ack ->
                assertThat(ack.id).isEqualTo("id")
                assertThat(ack.statusCode).isEqualTo(500)
                assertThat(ack.received).startsWith("Raw Failed Message from Producer:")
            }
            .verifyComplete()
    }
}