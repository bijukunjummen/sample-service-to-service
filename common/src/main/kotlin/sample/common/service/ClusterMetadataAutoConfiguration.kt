package sample.common.service

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@ConditionalOnBean(WebClient.Builder::class)
class ClusterMetadataAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Profile("!local")
    fun metadataClientOnGke(webClientBuilder: WebClient.Builder): MetadataClient {
        return WebClientMetadataClient(webClientBuilder)
    }

    @Bean
    @ConditionalOnMissingBean
    @Profile("local")
    fun metadataClientLocal(): MetadataClient {
        return LocalMetadataClient()
    }

}