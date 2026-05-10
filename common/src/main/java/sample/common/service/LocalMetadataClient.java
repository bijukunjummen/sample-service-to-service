package sample.common.service;

import reactor.core.publisher.Mono;

import java.net.InetAddress;

public class LocalMetadataClient implements MetadataClient {
    @Override
    public Mono<ClusterMetadata> getClusterInformation() {
        InetAddress address = InetLocalUtils.findFirstNonLoopbackAddress();
        String ipAddress = address != null ? address.toString() : "";
        return Mono.just(new ClusterMetadata(
                "local",
                "local",
                "local",
                ipAddress,
                "",
                ""
        ));
    }
}
