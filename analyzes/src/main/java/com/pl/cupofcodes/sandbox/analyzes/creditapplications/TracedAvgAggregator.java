package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
class TracedAvgAggregator {
    private final List<Map<String, String>> tracingContexts = new ArrayList<>();
    private BigDecimal sum = BigDecimal.ZERO;
    private long count = 0;

    public TracedAvgAggregator add(BigDecimal amount) {
        sum = sum.add(amount);
        count++;
        return this;
    }

    public BigDecimal average() {
        return count > 0 ? sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    public void linkTracing(Map<String, String> tracingContext) {
        this.tracingContexts.add(tracingContext);
    }
}
