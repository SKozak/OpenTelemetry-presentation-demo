package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditLimitSummary {
    private String documentTypeKey;
    private BigDecimal averageCreditLimit;
    private long startTime;
    private long endTime;
}
