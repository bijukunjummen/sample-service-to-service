package sample.producer.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sample.common.service.MetadataClient;
import sample.producer.config.RoutesConfig;
import sample.producer.model.Message;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

class MessageHandlerControllerTest {

    private MetadataClient metadataClient;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        metadataClient = mock(MetadataClient.class);
        MessageHandler messageHandler = new MessageHandler(metadataClient);
        RouterFunction<ServerResponse> routes = new RoutesConfig().apis(messageHandler);
        webTestClient = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void callToMessageEndpoint() {
        when(metadataClient.getClusterInformation()).thenReturn(Mono.empty());
        webTestClient.post().uri("/producer/messages")
                .body(fromValue(new Message("1", "one", 0)))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                        {
                          "id": "1",
                          "received": "one",
                          "headers": {}
                        }
                        """);
    }

    @Test
    void shouldThrowAnExceptionIfPayloadFlagIsSet() {
        when(metadataClient.getClusterInformation()).thenReturn(Mono.empty());
        webTestClient.post().uri("/producer/messages")
                .body(fromValue(new Message(
                        "1",
                        "one",
                        150,
                        500
                )))
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
