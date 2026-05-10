package sample.caller.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import sample.caller.model.Message;
import sample.caller.model.MessageAck;
import sample.caller.model.MessageAckLite;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RemoteMessageHandler implements MessageHandler {
    private static final String STOPWATCH_KEY = "stopWatch";

    private final WebClient.Builder webClientBuilder;
    private final String remoteBaseUrl;

    public RemoteMessageHandler(
            WebClient.Builder webClientBuilder,
            @Value("${remote.base.url}") String remoteBaseUrl
    ) {
        this.webClientBuilder = webClientBuilder;
        this.remoteBaseUrl = remoteBaseUrl;
    }

    @Override
    public Mono<MessageAck> handle(Message message, HttpHeaders callerHeaders) {
        WebClient webClient = webClientBuilder.build();
        URI uri = UriComponentsBuilder
                .fromUriString(remoteBaseUrl + "/producer/messages")
                .build()
                .toUri();
        Map<String, List<String>> callerHeadersMap = toMap(callerHeaders);
        return webClient.post()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(message))
                .exchangeToMono(response ->
                        Mono.<MessageAck>deferContextual(context -> {
                            StopWatch stopWatch = context.get(STOPWATCH_KEY);
                            stopWatch.stop();
                            long roundTripTime = stopWatch.getTotalTimeMillis();
                            if (response.statusCode().is2xxSuccessful()) {
                                return response
                                        .bodyToMono(MessageAckLite.class)
                                        .map(lite -> new MessageAck(
                                                lite.id(),
                                                lite.received(),
                                                callerHeadersMap,
                                                lite.headers(),
                                                null,
                                                lite.metadata(),
                                                response.statusCode().value(),
                                                roundTripTime
                                        ));
                            } else {
                                return response
                                        .bodyToMono(String.class)
                                        .map(responseAsString -> new MessageAck(
                                                message.id(),
                                                "Raw Failed Message from Producer: " + responseAsString,
                                                callerHeadersMap,
                                                Collections.emptyMap(),
                                                response.statusCode().value(),
                                                roundTripTime
                                        ));
                            }
                        })
                )
                .onErrorResume(t ->
                        Mono.<MessageAck>deferContextual(contextView -> {
                            StopWatch stopWatch = contextView.get(STOPWATCH_KEY);
                            stopWatch.stop();
                            long roundTripTime = stopWatch.getTotalTimeMillis();
                            return Mono.just(new MessageAck(
                                    message.id(),
                                    "Failed request: " + t.getMessage(),
                                    callerHeadersMap,
                                    Collections.emptyMap(),
                                    500,
                                    roundTripTime
                            ));
                        })
                )
                .contextWrite(context -> {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();
                    return context.put(STOPWATCH_KEY, stopWatch);
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
