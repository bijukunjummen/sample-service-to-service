package sample.common.service

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.web.reactive.function.client.WebClient

@AutoConfiguration
@ConditionalOnBean(WebClient.Builder::class)
class ClusterMetadataAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Profile("gcp")
    fun metadataClientOnGke(webClientBuilder: WebClient.Builder): MetadataClient {
        return WebClientGcpMetadataClient(webClientBuilder)
    }

    @Bean
    @ConditionalOnMissingBean
    fun metadataClientLocal(): MetadataClient {
        return LocalMetadataClient()
    }

}
