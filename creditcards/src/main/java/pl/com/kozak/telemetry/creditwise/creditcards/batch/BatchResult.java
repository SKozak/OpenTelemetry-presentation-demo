package pl.com.kozak.telemetry.creditwise.creditcards.batch;

import lombok.Value;

import java.math.BigDecimal;

@Value
class BatchResult {
    private String id;
    private BigDecimal averageCreditLimit;
    private int processedCount;
}
