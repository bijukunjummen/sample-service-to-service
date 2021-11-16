package sample.caller.web

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromValue
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import sample.caller.service.MessageHandler
import sample.common.service.MetadataClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [MessageController::class])
class MessageControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var messageHandler: MessageHandler

    @MockBean
    private lateinit var metadataClient: MetadataClient

    @Test
    fun testCallToMessageEndpoint() {
        whenever(metadataClient.getClusterInformation()).thenReturn(Mono.empty())
        whenever(messageHandler.handle(any(), any()))
                .thenAnswer { invocation ->
                    val originalMessage: Message = invocation.getArgument(0)
                    Mono.just(MessageAck(id = originalMessage.id,
                            received = originalMessage.payload,
                            callerHeaders = HttpHeaders(),
                            producerHeaders = emptyMap(),
                            statusCode = 200,
                            roundTripTimeMillis = 10L))
                }
        webTestClient.post().uri("/caller/messages")
                .body(fromValue(Message("1", "one", 0)))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json(""" 
                | {
                |   "id": "1",
                |   "received": "one",
                |   "callerHeaders": {},
                |   "producerHeaders": {},
                |   "statusCode": 200,
                |   "roundTripTimeMillis": 10
                | }
                """.trimMargin())
    }
}