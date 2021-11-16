package sample.common.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class WebClientMetadataClient(private val webClientBuilder: WebClient.Builder) : MetadataClient {
    override fun getClusterInformation(): Mono<ClusterMetadata> {
        return webClientBuilder.build()
                .get()
                .uri("http://metadata.google.internal/computeMetadata/v1/instance/attributes/?recursive=true")
                .header("Metadata-Flavor", "Google")
                .exchangeToMono { response ->
                    if (response.statusCode().is2xxSuccessful) {
                        response.bodyToMono<ClusterMetadata>()
                    } else {
                        Mono.empty()
                    }
                }
                .onErrorResume { t ->
                    LOGGER.error("Retrieving metadata failed", t)
                    Mono.empty()
                }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WebClientMetadataClient::class.java)
    }
}