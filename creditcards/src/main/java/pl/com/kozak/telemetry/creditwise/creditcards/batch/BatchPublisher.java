package pl.com.kozak.telemetry.creditwise.creditcards.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
class BatchPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(BatchResult batchResult) {
        log.info("Publishing batch result: {}", batchResult);
        final String payload;
        try {
            payload = objectMapper.writeValueAsString(batchResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        kafkaTemplate.send("batch-results", batchResult.getId(), payload);
    }
}
