package sample.common.service;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration
@ConditionalOnBean(WebClient.Builder.class)
public class ClusterMetadataAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Profile("gcp")
    public MetadataClient metadataClientOnGke(WebClient.Builder webClientBuilder) {
        return new WebClientGcpMetadataClient(webClientBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public MetadataClient metadataClientLocal() {
        return new LocalMetadataClient();
    }
}
