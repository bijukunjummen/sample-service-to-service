package sample.producer.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import sample.common.service.ClusterMetadata;
import sample.common.service.MetadataClient;
import sample.producer.model.Message;
import sample.producer.model.MessageAck;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    private final MetadataClient metadataClient;

    public MessageHandler(MetadataClient metadataClient) {
        this.metadataClient = metadataClient;
    }

    public Mono<ServerResponse> handleMessage(ServerRequest req) {
        return req.bodyToMono(Message.class).flatMap(m -> {
            LOGGER.info("Handling: {}", m);
            Map<String, List<String>> httpHeaders = toMap(req.headers().asHttpHeaders());
            return Mono
                    .fromCallable(() -> new MessageAck(m.id(), m.payload(), httpHeaders))
                    .delayElement(Duration.ofMillis(m.delay()))
                    .flatMap(messageAck -> metadataClient.getClusterInformation()
                            .switchIfEmpty(Mono.just(new ClusterMetadata("", "", "")))
                            .flatMap(metadata -> {
                                MessageAck withMetadata = messageAck.withMetadata(metadata);
                                return ServerResponse
                                        .status(HttpStatusCode.valueOf(m.responseCode()))
                                        .body(BodyInserters.fromValue(withMetadata));
                            }))
                    .doOnSuccess(r -> LOGGER.info("Completed Handling: {}", m))
                    .doOnError(t -> LOGGER.error("Error Handling: {}", m, t));
        });
    }

    private static Map<String, List<String>> toMap(HttpHeaders headers) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        for (String name : headers.headerNames()) {
            map.put(name, headers.get(name));
        }
        return map;
    }
}
