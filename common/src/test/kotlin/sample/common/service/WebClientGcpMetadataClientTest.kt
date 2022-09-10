package sample.common.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class WebClientGcpMetadataClientTest {

    @Test
    fun `retrieve metadata`() {
        val metadataBody =
            WebClientGcpMetadataClientTest::class.java
                .getResource("/sample-metadata-response.json")
                .readText()
        println(metadataBody)
        val clientResponse: ClientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(metadataBody)
                .build()
        val shortCircuitingExchangeFunction = ExchangeFunction {
            Mono.just(clientResponse)
        }
        val webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction)
        val webClientGcpMetadataClient = WebClientGcpMetadataClient(webClientBuilder)
        StepVerifier
                .create(webClientGcpMetadataClient.getClusterInformation())
                .assertNext { metadata ->
                    assertThat(metadata.clusterName).isEqualTo("cluster1")
                    assertThat(metadata.clusterLocation).isEqualTo("us-west1-a")
                }
                .verifyComplete()
    }
}
