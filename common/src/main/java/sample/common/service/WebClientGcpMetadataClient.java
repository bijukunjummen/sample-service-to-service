package sample.common.service;

import tools.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import sample.common.util.CachingUtils;

import java.net.InetAddress;
import java.time.Duration;

public class WebClientGcpMetadataClient implements MetadataClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientGcpMetadataClient.class);

    private final WebClient.Builder webClientBuilder;

    public WebClientGcpMetadataClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<ClusterMetadata> getClusterInformation() {
        Mono<ClusterMetadata> source = Mono.defer(() -> webClientBuilder.build()
                .get()
                .uri("http://metadata.google.internal/computeMetadata/v1/?recursive=true")
                .header("Metadata-Flavor", "Google")
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response
                                .bodyToMono(JsonNode.class)
                                .map(jsonNode -> {
                                    String clusterLocation = jsonNode.at("/instance/attributes/cluster-location").asText();
                                    String clusterName = jsonNode.at("/instance/attributes/cluster-name").asText();
                                    String hostName = jsonNode.at("/instance/hostname").asText();
                                    String region = jsonNode.at("/instance/region").asText();
                                    String zone = jsonNode.at("/instance/zone").asText();
                                    InetAddress address = InetLocalUtils.findFirstNonLoopbackAddress();
                                    String ipAddress = address != null ? address.toString() : "";
                                    return new ClusterMetadata(
                                            clusterName,
                                            clusterLocation,
                                            hostName,
                                            ipAddress,
                                            zone,
                                            region
                                    );
                                });
                    } else {
                        return Mono.<ClusterMetadata>empty();
                    }
                })
                .onErrorResume(t -> {
                    LOGGER.error("Retrieving metadata failed", t);
                    return Mono.<ClusterMetadata>empty();
                }));
        return CachingUtils.ofMonoFixedKey(Duration.ofHours(2), source);
    }
}
