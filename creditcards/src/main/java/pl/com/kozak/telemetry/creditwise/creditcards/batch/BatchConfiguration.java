package pl.com.kozak.telemetry.creditwise.creditcards.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@RequiredArgsConstructor
class BatchConfiguration {
    private final OpenTelemetry openTelemetry;
    private final ObjectMapper objectMapper;

    @Bean
    BatchPublisher batchPublisher(KafkaTemplate<String, String> defaultKafkaTemplate) {
        return new BatchPublisher(defaultKafkaTemplate, objectMapper);
    }

    @Bean
    ApplicationBatchProcessing applicationBatchProcessing(BatchPublisher batchPublisher) {
        return new ApplicationBatchProcessing(batchPublisher, openTelemetry.getTracer("batchTracer"), openTelemetry);
    }
}
