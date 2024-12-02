package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.debezium;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.Outbox;

@Configuration
@RequiredArgsConstructor
class DebeziumOutboxConfiguration {
    private final OpenTelemetry openTelemetry;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    Outbox debeziumOutbox(DebeziumOutboxRepository debeziumOutboxRepository) {
        return new DebeziumBackedOutbox(openTelemetry, objectMapper, debeziumOutboxRepository);
    }

    @Bean
    DebeziumOutboxRepository debeziumOutboxRepository() {
        return new DebeziumOutboxRepository(jdbcTemplate);
    }
}
