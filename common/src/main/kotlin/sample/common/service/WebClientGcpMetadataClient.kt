package sample.common.service

import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import sample.common.util.CachingUtils
import java.time.Duration

class WebClientGcpMetadataClient(private val webClientBuilder: WebClient.Builder) : MetadataClient {
    override fun getClusterInformation(): Mono<ClusterMetadata> {
        return CachingUtils.ofMonoFixedKey(
            Duration.ofHours(2),
            Mono.defer {
                webClientBuilder.build()
                    .get()
                    .uri("http://metadata.google.internal/computeMetadata/v1/?recursive=true")
                    .header("Metadata-Flavor", "Google")
                    .exchangeToMono { response ->
                        if (response.statusCode().is2xxSuccessful) {
                            response
                                .bodyToMono<JsonNode>()
                                .map { jsonNode ->
                                    val clusterLocation = jsonNode.at("/instance/attributes/cluster-location").asText()
                                    val clusterName = jsonNode.at("/instance/attributes/cluster-name").asText()
                                    val hostName = jsonNode.at("/instance/hostname").asText()
                                    ClusterMetadata(
                                        clusterLocation = clusterLocation,
                                        clusterName = clusterName,
                                        hostName = hostName,
                                        ipAddress = InetLocalUtils.findFirstNonLoopbackAddress()?.toString() ?: ""
                                    )
                                }
                        } else {
                            Mono.empty()
                        }
                    }
                    .onErrorResume { t ->
                        LOGGER.error("Retrieving metadata failed", t)
                        Mono.empty()
                    }
            }
        )
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(WebClientGcpMetadataClient::class.java)
    }
}
