package sample.common.service;

import reactor.core.publisher.Mono;

public interface MetadataClient {
    Mono<ClusterMetadata> getClusterInformation();
}
