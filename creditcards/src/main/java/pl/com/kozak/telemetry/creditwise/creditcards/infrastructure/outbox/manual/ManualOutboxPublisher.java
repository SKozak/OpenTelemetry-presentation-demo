package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.manual;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import pl.com.kozak.telemetry.creditwise.creditcards.shared.tracing.TracingContextMapExtractor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class ManualOutboxPublisher {
    private final ManualOutboxRepository manualOutboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final TextMapPropagator textMapPropagator;
    private final Tracer tracer;

    public ManualOutboxPublisher(ManualOutboxRepository manualOutboxRepository, KafkaTemplate<String, String> kafkaTemplate, OpenTelemetry openTelemetry,
                                 ObjectMapper objectMapper) {
        this.manualOutboxRepository = manualOutboxRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
        this.objectMapper = objectMapper;
        this.tracer = openTelemetry.getTracer("OutboxPublisher");
    }

    @Transactional
    @Scheduled(fixedDelay = 100) // 0,5 second
    public void publishEventsScheduler() throws JsonProcessingException {
        List<OutboxMessage> events = manualOutboxRepository.findAllByPublishedFalse(PageRequest.of(0, 20)).getContent();
        for (OutboxMessage event : events) {
            sendEvent(event);
            event.markPublished();
            manualOutboxRepository.save(event);
        }
    }

    private void sendEvent(OutboxMessage event) throws JsonProcessingException {
        Map<String, String> map = objectMapper.readValue(event.getTracingData(), new TypeReference<HashMap<String, String>>() {});
        Context extractedContext = textMapPropagator.extract(Context.current(), map, new TracingContextMapExtractor());
        final SpanContext currentSpanToLink = Span.current().getSpanContext();

        try (Scope unused = extractedContext.makeCurrent()) {
            Span serverSpan = tracer.spanBuilder("outbox-kafka-producer")
                .addLink(currentSpanToLink)
                .setSpanKind(SpanKind.INTERNAL)
                .startSpan();
            try {
                kafkaTemplate.send(event.getDestination(), event.getKey(), event.getPayload());
                log.info("Event was sent from outbox: {}", event.getPayload());
            } catch (Exception e) {
                serverSpan.recordException(e);
                throw e;
            } finally {
                serverSpan.end();
            }
        }
    }
}
