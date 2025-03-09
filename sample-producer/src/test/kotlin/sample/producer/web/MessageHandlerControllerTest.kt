package sample.producer.web

import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromValue
import reactor.core.publisher.Mono
import sample.common.service.MetadataClient
import sample.producer.config.RoutesConfig
import sample.producer.model.Message

@WebFluxTest(controllers = [RoutesConfig::class, MessageHandler::class])
class MessageHandlerControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockitoBean
    private lateinit var metadataClient: MetadataClient

    @Test
    fun `call to message endpoint`() {
        whenever(metadataClient.getClusterInformation()).thenReturn(Mono.empty())
        webTestClient.post().uri("/producer/messages")
                .body(fromValue(Message("1", "one", 0)))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(""" 
                | {
                |   "id": "1",
                |   "received": "one",
                |   "headers": {}
                | }
                """.trimMargin())
    }

    @Test
    fun `should throw an exception if the payload flag is set`() {
        whenever(metadataClient.getClusterInformation()).thenReturn(Mono.empty())
        webTestClient.post().uri("/producer/messages")
                .body(fromValue(Message(
                        id = "1",
                        payload = "one",
                        delay = 150,
                        responseCode = 500
                )))
                .exchange()
                .expectStatus().is5xxServerError
    }

}