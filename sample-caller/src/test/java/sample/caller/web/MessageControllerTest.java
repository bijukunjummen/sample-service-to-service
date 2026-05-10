package sample.caller.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import sample.caller.model.Message;
import sample.caller.model.MessageAck;
import sample.caller.service.MessageHandler;
import sample.common.service.MetadataClient;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

class MessageControllerTest {

    private MessageHandler messageHandler;
    private MetadataClient metadataClient;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        messageHandler = mock(MessageHandler.class);
        metadataClient = mock(MetadataClient.class);
        MessageController controller = new MessageController(messageHandler, metadataClient);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void testCallToMessageEndpoint() {
        when(metadataClient.getClusterInformation()).thenReturn(Mono.empty());
        when(messageHandler.handle(any(), any()))
                .thenAnswer(invocation -> {
                    Message originalMessage = invocation.getArgument(0);
                    return Mono.just(new MessageAck(
                            originalMessage.id(),
                            originalMessage.payload(),
                            Collections.emptyMap(),
                            Collections.emptyMap(),
                            200,
                            10L
                    ));
                });
        webTestClient.post().uri("/caller/messages")
                .body(fromValue(new Message("1", "one", 0)))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                          "id": "1",
                          "received": "one",
                          "callerHeaders": {},
                          "producerHeaders": {},
                          "statusCode": 200,
                          "roundTripTimeMillis": 10
                        }
                        """);
    }
}
