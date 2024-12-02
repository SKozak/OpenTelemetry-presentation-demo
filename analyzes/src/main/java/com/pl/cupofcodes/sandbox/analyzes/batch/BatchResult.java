package com.pl.cupofcodes.sandbox.analyzes.batch;

import lombok.Data;

import java.math.BigDecimal;

@Data
class BatchResult {
    private String id;
    private BigDecimal averageCreditLimit;
    private int processedCount;
}
