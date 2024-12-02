package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

@Slf4j
class TracingProcessor implements Processor<String, ValueAndTrace<TracedAvgAggregator>, String, ValueAndTrace<TracedAvgAggregator>> {
    private static final String AGGREGATE_PROCESS_OUT = "aggregate-process-out";
    private final TracingProvider tracingProvider;
    private ProcessorContext<String, ValueAndTrace<TracedAvgAggregator>> context;

    @Override
    public void init(ProcessorContext<String, ValueAndTrace<TracedAvgAggregator>> context) {
        this.context = context;
    }

    public TracingProcessor(TracingProvider tracingProvider) {
        this.tracingProvider = tracingProvider;
    }

    @Override
    public void process(Record<String, ValueAndTrace<TracedAvgAggregator>> avgAggregatorRecord) {
        log.info("custom processor record: {}", avgAggregatorRecord);
        final String parentContextId = avgAggregatorRecord.value().getParentContext();
        Context parentContext;

        if (parentContextId != null) {
            parentContext = tracingProvider.contextFromTraceIdString(parentContextId);
        } else {
            parentContext = Context.current();
        }
        Span aggregateOutSpan = tracingProvider.startSpan(AGGREGATE_PROCESS_OUT, parentContext, null);

        try (Scope scope = aggregateOutSpan.makeCurrent()) {
            log.info("Starting new aggregateOutSpan with linkins:  {}", aggregateOutSpan);
            context.forward(avgAggregatorRecord);
        } finally {
            log.info("Ending aggregateOutSpan: {}", aggregateOutSpan);
            aggregateOutSpan.end();
        }
    }
}
