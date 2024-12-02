package com.pl.cupofcodes.sandbox.analyzes.creditapplications.sink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pl.cupofcodes.sandbox.analyzes.creditapplications.CreditLimitSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
class CreditLimitSumarySink {

    private final ObjectMapper objectMapper;
    private final CreditLimitSummaryRepository repository;

    @KafkaListener(topics = "averageCreditLimitByDocumentType-traced")
    public void consume(String message) {
        try {
            log.info("Consuming message {}", message);
            CreditLimitSummary summaryDocument = objectMapper.readValue(message, CreditLimitSummary.class);
            repository.save(toDocument(summaryDocument));
            log.info("Message consumed");
        } catch (Exception e) {
            log.error("Error consuming message {}", message, e);
        }
    }

    private CreditLimitSummaryDocument toDocument(CreditLimitSummary summaryDocument) {
        return new CreditLimitSummaryDocument(
            UUID.randomUUID().toString(),
            summaryDocument.getDocumentTypeKey(),
            summaryDocument.getAverageCreditLimit(),
            summaryDocument.getStartTime(),
            summaryDocument.getEndTime()
        );
    }

}
