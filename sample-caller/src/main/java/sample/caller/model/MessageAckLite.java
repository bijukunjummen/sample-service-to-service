package sample.caller.model;

import sample.common.service.ClusterMetadata;

import java.util.List;
import java.util.Map;

public record MessageAckLite(
        String id,
        String received,
        Map<String, List<String>> headers,
        ClusterMetadata metadata
) {
}
