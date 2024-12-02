package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.manual;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapPropagator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.Outbox;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.OutboxEvent;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.manual.OutboxMessage.OutboxMessageBuilder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ManualOutbox implements Outbox {
    private final OpenTelemetry openTelemetry;
    private final ObjectMapper objectMapper;
    private final ManualOutboxRepository manualOutboxRepository;

    public void save(OutboxEvent outboxEvent) {
        saveToDatabase(outboxEvent);
    }

    private void saveToDatabase(OutboxEvent outboxEvent) {
        final Map<String, String> map = new HashMap<>();
        final TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
        textMapPropagator.inject(Context.current(), map, Map::put);
        log.info("current Tracing data: {}", map);

        try {
            OutboxMessageBuilder outboxMessageBuilder = OutboxMessage.builder();
            outboxMessageBuilder.sourceId(outboxEvent.getSourceId());
            outboxMessageBuilder.destination(outboxEvent.getDestination());
            outboxMessageBuilder.sourceType(outboxEvent.getSourceType());
            outboxMessageBuilder.payload(objectMapper.writeValueAsString(outboxEvent));
            outboxMessageBuilder.published(false);
            outboxMessageBuilder.createdAt(Instant.now());
            outboxMessageBuilder.tracingData(objectMapper.writeValueAsString(map));
            outboxMessageBuilder.key(outboxEvent.getKey());
            final OutboxMessage outboxMessage = outboxMessageBuilder.build();
            manualOutboxRepository.save(outboxMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
