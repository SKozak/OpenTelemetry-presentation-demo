package com.pl.cupofcodes.sandbox.analyzes.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
@AllArgsConstructor
class BatchResultDocument {
    @Id
    private String id;
    private BigDecimal averageCreditLimit;
    private int processedCount;
}
