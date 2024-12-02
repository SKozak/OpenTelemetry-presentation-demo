package com.pl.cupofcodes.sandbox.analyzes.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class BatchResultListener {

    private final BatchResultRepository batchResultRepository;

    @KafkaListener(topics = "batch-results", containerFactory = "batchKafkaListenerContainerFactory")
    public void consume(BatchResult batchResult) {
        log.info("Consuming batchResult {}", batchResult);
        final BatchResultDocument batchResultDocument = new BatchResultDocument(batchResult.getId(), batchResult.getAverageCreditLimit(), batchResult.getProcessedCount());
        batchResultRepository.save(batchResultDocument);
    }

}
