package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.debezium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapPropagator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.Outbox;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.OutboxEvent;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.debezium.DebeziumOutboxMessage.DebeziumOutboxMessageBuilder;

import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class DebeziumBackedOutbox implements Outbox {
    private final OpenTelemetry openTelemetry;
    private final ObjectMapper objectMapper;
    private final DebeziumOutboxRepository outboxRepository;

    @Override
    public void save(OutboxEvent outboxEvent) {
        saveToDatabase(outboxEvent);
    }

    private void saveToDatabase(OutboxEvent outboxEvent) {
        final Properties properties = new Properties();

        final TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
        textMapPropagator.inject(Context.current(), properties, Map::put);

        log.info("current Tracing data: {}", properties);

        String propertiesAsString = properties.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));

        try {
            final DebeziumOutboxMessageBuilder builder = DebeziumOutboxMessage.builder();
            builder.id(UUID.randomUUID());
            builder.aggregatetype(outboxEvent.getDestination());
            builder.aggregateid(outboxEvent.getKey());
            builder.payload(objectMapper.writeValueAsString(outboxEvent));
            builder.published(false);
            builder.createdAt(Instant.now());
            // needed if using manual set header for outbox message in debezium
            builder.trace(properties.getProperty("traceparent"));
            // needed if using open telemetry debezium integration in debezium instance
            builder.tracingspancontext(propertiesAsString);
            builder.sourceType(outboxEvent.getSourceType());
            builder.sourceid(outboxEvent.getSourceId());
            final DebeziumOutboxMessage debeziumOutboxMessage = builder.build();
            outboxRepository.save(debeziumOutboxMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
