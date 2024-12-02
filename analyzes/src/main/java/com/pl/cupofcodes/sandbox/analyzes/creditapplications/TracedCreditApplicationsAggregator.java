package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
class TracedCreditApplicationsAggregator {
    private static final String AGGREGATION_PROCESS_IN = "aggregation-process-in";
    private final TracingProvider kstreamTracingProvider;

    @Bean
    public KStream<String, ApplicationProcessedEvent> applicationProcessedEventStream(StreamsBuilder streamsBuilder) {
        Serde<String> stringSerde = Serdes.String();
        JsonSerde<ApplicationProcessedEvent> applicationProcessedEventSerde = new JsonSerde<>(ApplicationProcessedEvent.class);
        applicationProcessedEventSerde.ignoreTypeHeaders();

        KStream<String, ApplicationProcessedEvent> applicationStream = streamsBuilder.stream("application-processed-events", Consumed.with(stringSerde, applicationProcessedEventSerde));

        ValueAndTraceSerde<TracedAvgAggregator> longValueAndTraceSerde = new ValueAndTraceSerde<>(new JsonSerde<>(TracedAvgAggregator.class));
        JsonSerde<CreditLimitSummary> creditLimitSummarySerde = new JsonSerde<>(CreditLimitSummary.class);

        KTable<String, ValueAndTrace<TracedAvgAggregator>> averageCreditLimitByDocumentType = applicationStream
            .peek((key, value) -> log.info("Received application: {}, State: {}", key, value.applicationState()))
            .filter((key, value) -> value.applicationState() == ApplicationState.APPROVED)
            .map((key, value) -> {
                log.info("Mapping: {}, Credit limit: {}", key, value.creditLimit());
                return new KeyValue<>(value.type().toString(), value);
            })
            .groupByKey(Grouped.with(stringSerde, applicationProcessedEventSerde))
            .aggregate(() -> new ValueAndTrace<>(null, new TracedAvgAggregator()), (key, value, aggregate) -> {
                log.info("start aggregation");
                String currentTraceParent = kstreamTracingProvider.getCurrentTraceParentAsString();
                Span aggregateSpan  = kstreamTracingProvider.startSpan(AGGREGATION_PROCESS_IN, Context.current(), null);
                ValueAndTrace<TracedAvgAggregator> result;
                try (Scope ignored = aggregateSpan.makeCurrent()) {
                    result = new ValueAndTrace<>(currentTraceParent, aggregate.getValue().add(value.creditLimit()));
                } finally {
                    aggregateSpan.end();
                }
                log.info("end aggregation");
                return result;
            }, Materialized.with(Serdes.String(), longValueAndTraceSerde));

        averageCreditLimitByDocumentType.toStream()
            .process(() -> new TracingProcessor(kstreamTracingProvider))
            .map(this::mapToSummary)
            .peek((key, value) -> log.info("Publishing summary for: {}, Average: {}", key, value.getAverageCreditLimit()))
            .to("averageCreditLimitByDocumentType-traced", Produced.with(stringSerde, creditLimitSummarySerde));

        Topology topology = streamsBuilder.build();
        log.info("Topology: {}", topology.describe());

        return applicationStream;
    }

//    @Bean
//    public KStream<String, ApplicationProcessedEvent> applicationProcessedEventStream(StreamsBuilder streamsBuilder) {
//        Serde<String> stringSerde = Serdes.String();
//        JsonSerde<ApplicationProcessedEvent> applicationProcessedEventSerde = new JsonSerde<>(ApplicationProcessedEvent.class);
//
//        KStream<String, ApplicationProcessedEvent> applicationStream = streamsBuilder.stream("application-processed-events",
//            Consumed.with(stringSerde,
//                applicationProcessedEventSerde));
//
//        ValueAndTraceSerde<TracedAvgAggregator> longValueAndTraceSerde = new ValueAndTraceSerde<>(new JsonSerde<>(TracedAvgAggregator.class));
//        JsonSerde<CreditLimitSummary> creditLimitSummarySerde = new JsonSerde<>(CreditLimitSummary.class);
//
//        KTable<Windowed<String>, ValueAndTrace<TracedAvgAggregator>> averageCreditLimitByDocumentType = applicationStream
//            .peek((key, value) -> log.info("Received application: {}, State: {}", key, value.applicationState()))
//            .filter((key, value) -> value.applicationState() == ApplicationState.APPROVED)
//            .map((key, value) -> {
//                log.info("Mapping: {}, Credit limit: {}", key, value.creditLimit());
//                return new KeyValue<>(value.type().toString(), value);
//            })
//            .groupByKey(Grouped.with(stringSerde, applicationProcessedEventSerde))
//            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
//            .aggregate(() -> new ValueAndTrace<>(null, new TracedAvgAggregator()), (key, value, aggregate) -> {
//                Map<String, String> currentTraceContext = tracingProvider.getContext();
//                Map<String, String> parentContext = Map.of("traceparent", aggregate.getParentContext());
//                SpanBuilder aggregateSpanBuilder = tracingProvider.createLinkedSpan("aggregate-process", Context.current(), parentContext);
//                Span aggregateSpan = aggregateSpanBuilder.startSpan();
//
//                ValueAndTrace<TracedAvgAggregator> result;
//                try (Scope ignored = aggregateSpan.makeCurrent()) {
//                    result = new ValueAndTrace<>(currentTraceContext.get("traceparent"), aggregate.getValue().add(value.creditLimit()));
//                } finally {
//                    aggregateSpan.end();
//                }
//                return result;
//            }, Materialized.with(Serdes.String(), longValueAndTraceSerde));
//
//        averageCreditLimitByDocumentType.toStream()
//            .process(() -> new TracingProcessor(tracingProvider))
//            .map(this::mapToSummary)
//            .peek((key, value) -> log.info("Publishing summary for: {}, Average: {}", key, value.getAverageCreditLimit()))
//            .to("averageCreditLimitByDocumentType", Produced.with(stringSerde, creditLimitSummarySerde));
//
//        return applicationStream;
//    }

    private KeyValue<String, CreditLimitSummary> mapToSummary(String key, ValueAndTrace<TracedAvgAggregator> valueAndTrace) {
        log.info("Mapping to summary: {}, Average: {}", key, valueAndTrace.getValue().average());
        return new KeyValue<>(
            key,
            new CreditLimitSummary(key, valueAndTrace.getValue().average().setScale(2, RoundingMode.HALF_UP), 0L, 0L)
        );
    }


    //            .aggregate(
//                TracedAvgAggregator::new,
//                (key, value, aggregate) -> {
//                    log.info("Aggregating: {}, Credit limit: {}", key, value.creditLimit());
//                    aggregate.linkTracing(tracingProvider.getContext());
//                    return aggregate.add(value.creditLimit());
//                },
//                Materialized.with(stringSerde, new JsonSerde<>(TracedAvgAggregator.class))

}
