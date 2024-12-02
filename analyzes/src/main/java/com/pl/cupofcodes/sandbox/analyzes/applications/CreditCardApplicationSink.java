package com.pl.cupofcodes.sandbox.analyzes.applications;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class CreditCardApplicationSink {

    private final ObjectMapper objectMapper;
    private final CreditCardApplicationRepository repository;

    @KafkaListener(topics = "outbox.event.application-processed-events")
    public void consume(String message) {
        try {
            log.info("Consuming message {}", message);
            ApplicationProcessedEvent applicationProcessedEvent = objectMapper.readValue(message, ApplicationProcessedEvent.class);
            repository.save(toDocument(applicationProcessedEvent));
            log.info("Message consumed");
        } catch (Exception e) {
            log.error("Error consuming message {}", message, e);
        }
    }

    private CreditCardApplicationDocument toDocument(ApplicationProcessedEvent event) {
        return new CreditCardApplicationDocument(
                event.id(),
                event.creditLimit()
        );
    }

}
