package sample.producer.model;

import sample.common.service.ClusterMetadata;

import java.util.List;
import java.util.Map;

public record MessageAck(
        String id,
        String received,
        Map<String, List<String>> headers,
        ClusterMetadata metadata
) {
    public MessageAck(String id, String received, Map<String, List<String>> headers) {
        this(id, received, headers, null);
    }

    public MessageAck withMetadata(ClusterMetadata metadata) {
        return new MessageAck(this.id, this.received, this.headers, metadata);
    }
}
