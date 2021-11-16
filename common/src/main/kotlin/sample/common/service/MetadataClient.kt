package sample.common.service

import reactor.core.publisher.Mono

interface MetadataClient {
    fun getClusterInformation(): Mono<ClusterMetadata>
}