package sample.producer.config

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MetricConfig {
    @Bean
    fun configurer(
        @Value("\${spring.application.name}") applicationName: String
    ): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry: MeterRegistry ->
            registry.config().commonTags("application", applicationName)
        }
    }
}