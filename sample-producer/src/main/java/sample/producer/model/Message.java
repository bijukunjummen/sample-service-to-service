package sample.producer.model;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public record Message(
        String id,
        String payload,
        long delay,
        int responseCode
) {
    public Message(String id, String payload, long delay) {
        this(id, payload, delay, HttpStatus.OK.value());
    }

    public Message(String payload, long delay) {
        this(UUID.randomUUID().toString(), payload, delay, HttpStatus.OK.value());
    }
}
