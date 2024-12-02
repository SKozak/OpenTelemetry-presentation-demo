package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
class AvgAggregator {
    private BigDecimal sum = BigDecimal.ZERO;
    private long count = 0;

    public AvgAggregator add(BigDecimal amount) {
        sum = sum.add(amount);
        count++;
        return this;
    }

    public BigDecimal average() {
        return count > 0 ? sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }
}
