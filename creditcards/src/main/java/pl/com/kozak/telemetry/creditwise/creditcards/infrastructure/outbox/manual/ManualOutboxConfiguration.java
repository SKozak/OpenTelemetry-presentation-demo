package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.manual;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.Outbox;

@Configuration
@RequiredArgsConstructor
class ManualOutboxConfiguration {
    private final OpenTelemetry openTelemetry;
    private final ObjectMapper objectMapper;

    @Bean
    Outbox manualOutbox(ManualOutboxRepository manualOutboxRepository) {
        return new ManualOutbox(openTelemetry, objectMapper, manualOutboxRepository);
    }

    @Bean
    ManualOutboxPublisher outboxPublisher(ManualOutboxRepository manualOutboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        return new ManualOutboxPublisher(manualOutboxRepository, kafkaTemplate, openTelemetry, objectMapper);
    }
}
