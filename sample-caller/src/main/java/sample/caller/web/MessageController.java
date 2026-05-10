package sample.caller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sample.caller.model.Message;
import sample.caller.model.MessageAck;
import sample.caller.service.MessageHandler;
import sample.common.service.ClusterMetadata;
import sample.common.service.MetadataClient;

@RestController
public class MessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final MessageHandler messageHandler;
    private final MetadataClient metadataClient;

    public MessageController(MessageHandler messageHandler, MetadataClient metadataClient) {
        this.messageHandler = messageHandler;
        this.metadataClient = metadataClient;
    }

    @PostMapping("/caller/messages")
    public Mono<MessageAck> handle(@RequestBody Message message, @RequestHeader HttpHeaders callerHeaders) {
        LOGGER.info("Handling message {}", message);
        return messageHandler.handle(message, callerHeaders)
                .flatMap(messageAck -> metadataClient.getClusterInformation()
                        .switchIfEmpty(Mono.just(new ClusterMetadata("", "", "")))
                        .map(messageAck::withCallerMetadata));
    }
}
