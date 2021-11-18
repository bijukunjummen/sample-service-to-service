package sample.common.service

import reactor.core.publisher.Mono

class LocalMetadataClient : MetadataClient {
    override fun getClusterInformation(): Mono<ClusterMetadata> {
        return Mono.just(ClusterMetadata(clusterName = "local", clusterLocation = "local"))
    }
}