package sample.meter.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import sample.meter.MessageHandler


@Configuration
class RoutesConfig {

    @Value("classpath:/static/index.html")
    private lateinit var indexHtml: Resource

    @Bean
    fun apis(messageHandler: MessageHandler) = router {
        (accept(MediaType.APPLICATION_JSON) and "/messages").nest {
            POST( messageHandler::handleMessage)
        }
        GET("/", { ServerResponse.ok().bodyValue(indexHtml) })
    }

}
