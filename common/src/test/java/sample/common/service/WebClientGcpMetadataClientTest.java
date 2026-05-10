package sample.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientGcpMetadataClientTest {

    @Test
    void retrieveMetadata() throws IOException {
        String metadataBody;
        try (InputStream is = WebClientGcpMetadataClientTest.class.getResourceAsStream("/sample-metadata-response.json")) {
            assertThat(is).isNotNull();
            metadataBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        ClientResponse clientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(metadataBody)
                .build();
        ExchangeFunction shortCircuitingExchangeFunction = req -> Mono.just(clientResponse);
        WebClient.Builder webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction);
        WebClientGcpMetadataClient webClientGcpMetadataClient = new WebClientGcpMetadataClient(webClientBuilder);
        StepVerifier
                .create(webClientGcpMetadataClient.getClusterInformation())
                .assertNext(metadata -> {
                    assertThat(metadata.clusterName()).isEqualTo("cluster1");
                    assertThat(metadata.clusterLocation()).isEqualTo("us-west1-a");
                })
                .verifyComplete();
    }
}
