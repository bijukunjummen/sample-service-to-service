package sample.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import sample.producer.web.MessageHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RoutesConfig {
    @Bean
    public RouterFunction<ServerResponse> apis(MessageHandler messageHandler) {
        return RouterFunctions.route(
                POST("/producer/messages").and(accept(MediaType.APPLICATION_JSON)),
                messageHandler::handleMessage
        );
    }
}
