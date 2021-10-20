package sample.caller.web

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters.fromValue
import reactor.core.publisher.Mono
import sample.caller.model.Message
import sample.caller.model.MessageAck
import sample.caller.service.MessageHandler

@ExtendWith(SpringExtension::class)
@WebFluxTest(controllers = [MessageController::class])
class MessageControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var messageHandler: MessageHandler

    @Test
    fun testCallToMessageEndpoint() {
        whenever(messageHandler.handle(any()))
            .thenAnswer { invocation ->
                val originalMessage: Message = invocation.getArgument<Message>(0)
                Mono.just(MessageAck(id = originalMessage.id, received = originalMessage.payload, "ack"))
            }
        webTestClient.post().uri("/caller/messages")
            .body(fromValue(Message("1", "one", 0)))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .json(
                """ 
                    | {
                    |   "id": "1",
                    |   "received": "one",
                    |   "ack": "ack"
                    | }
                """.trimMargin()
            )
    }
}