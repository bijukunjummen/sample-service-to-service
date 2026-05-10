package sample.caller.model;

import sample.common.service.ClusterMetadata;

import java.util.List;
import java.util.Map;

public record MessageAck(
        String id,
        String received,
        Map<String, List<String>> callerHeaders,
        Map<String, List<String>> producerHeaders,
        ClusterMetadata callerMetadata,
        ClusterMetadata producerMetadata,
        int statusCode,
        long roundTripTimeMillis
) {
    public MessageAck(
            String id,
            String received,
            Map<String, List<String>> callerHeaders,
            Map<String, List<String>> producerHeaders,
            int statusCode,
            long roundTripTimeMillis
    ) {
        this(id, received, callerHeaders, producerHeaders, null, null, statusCode, roundTripTimeMillis);
    }

    public MessageAck(
            String id,
            String received,
            Map<String, List<String>> callerHeaders,
            Map<String, List<String>> producerHeaders,
            ClusterMetadata producerMetadata,
            int statusCode,
            long roundTripTimeMillis
    ) {
        this(id, received, callerHeaders, producerHeaders, null, producerMetadata, statusCode, roundTripTimeMillis);
    }

    public MessageAck withCallerMetadata(ClusterMetadata callerMetadata) {
        return new MessageAck(
                this.id,
                this.received,
                this.callerHeaders,
                this.producerHeaders,
                callerMetadata,
                this.producerMetadata,
                this.statusCode,
                this.roundTripTimeMillis
        );
    }
}
