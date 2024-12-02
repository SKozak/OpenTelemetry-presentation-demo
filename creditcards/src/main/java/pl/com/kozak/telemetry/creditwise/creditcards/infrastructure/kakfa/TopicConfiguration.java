package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.kakfa;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class TopicConfiguration {
    @Bean
    public NewTopic compactTopicExample() {
        return TopicBuilder.name("application-processed-events")
            .partitions(1)
            .replicas(1)
            .build();
    }
}
