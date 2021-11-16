package sample.common.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class WebClientMetadataClientTest {

    @Test
    fun `retrieve metadata`() {
        val clientResponse: ClientResponse = ClientResponse
                .create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("""
                | {
                |    "clusterLocation":"us-west1-a",
                |    "clusterName":"cluster1",
                |    "clusterUid":"a7c67f3eeb0f4676b6da4d72489561016b918d768ebf4d878aebf909f5d5c5ac"
                | } 
                 """.trimMargin().trimIndent())
                .build()
        val shortCircuitingExchangeFunction = ExchangeFunction {
            Mono.just(clientResponse)
        }
        val webClientBuilder = WebClient.builder().exchangeFunction(shortCircuitingExchangeFunction)
        val webClientMetadataClient = WebClientMetadataClient(webClientBuilder)
        StepVerifier
                .create(webClientMetadataClient.getClusterInformation())
                .assertNext { metadata ->
                    assertThat(metadata.clusterName).isEqualTo("cluster1")
                    assertThat(metadata.clusterLocation).isEqualTo("us-west1-a")
                }
                .verifyComplete()
    }
}