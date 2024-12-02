package com.pl.cupofcodes.sandbox.analyzes.creditapplications.sink;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
@AllArgsConstructor
public class CreditLimitSummaryDocument {
    @Id
    private String id;
    private String documentTypeKey;
    private BigDecimal averageCreditLimit;
    private long startTime;
    private long endTime;

}
