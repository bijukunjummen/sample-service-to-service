package sample.caller.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sample.caller.model.Message;

import static org.assertj.core.api.Assertions.assertThat;

class RemoteMessageHandlerTest {

    @Test
    void aCleanFlow() {
        ClientResponse clientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("""
                        {
                          "id": "123",
                          "received": "test",
                          "headers": {},
                          "metadata": {"clusterName": "test", "clusterLocation": "us-west1"}
                        }
                        """)
                .build();
        ExchangeFunction shortCircuitingExchangeFunction = req -> Mono.just(clientResponse);
        WebClient.Builder webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction);
        RemoteMessageHandler remoteMessageHandler = new RemoteMessageHandler(webClientBuilder, "http://someurl");
        StepVerifier
                .create(remoteMessageHandler.handle(
                        new Message("id", "test", 1L, 200),
                        new HttpHeaders()
                ))
                .assertNext(ack -> {
                    assertThat(ack.id()).isEqualTo("123");
                    assertThat(ack.statusCode()).isEqualTo(200);
                    assertThat(ack.producerMetadata()).isNotNull();
                    assertThat(ack.producerMetadata().clusterName()).isEqualTo("test");
                })
                .verifyComplete();
    }

    @Test
    void anErrorFlow() {
        ClientResponse clientResponse = ClientResponse
                .create(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json")
                .body("""
                        {
                          "id": "123",
                          "received": "test",
                          "headers": {}
                        }
                        """)
                .build();
        ExchangeFunction shortCircuitingExchangeFunction = req -> Mono.just(clientResponse);
        WebClient.Builder webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction);
        RemoteMessageHandler remoteMessageHandler = new RemoteMessageHandler(webClientBuilder, "http://someurl");
        StepVerifier
                .create(remoteMessageHandler.handle(
                        new Message("id", "test", 1L, 500),
                        new HttpHeaders()
                ))
                .assertNext(ack -> {
                    assertThat(ack.id()).isEqualTo("id");
                    assertThat(ack.statusCode()).isEqualTo(500);
                    assertThat(ack.received()).startsWith("Raw Failed Message from Producer:");
                })
                .verifyComplete();
    }
}
