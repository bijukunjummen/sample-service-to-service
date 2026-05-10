package sample.caller.service;

import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;
import sample.caller.model.Message;
import sample.caller.model.MessageAck;

public interface MessageHandler {
    Mono<MessageAck> handle(Message message, HttpHeaders callerHeaders);
}
