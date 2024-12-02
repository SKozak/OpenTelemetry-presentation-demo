//package com.pl.cupofcodes.sandbox.analyzes.creditapplications;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.streams.KeyValue;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.Topology;
//import org.apache.kafka.streams.kstream.Consumed;
//import org.apache.kafka.streams.kstream.Grouped;
//import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.kstream.KTable;
//import org.apache.kafka.streams.kstream.Materialized;
//import org.apache.kafka.streams.kstream.Produced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.support.serializer.JsonSerde;
//import org.springframework.stereotype.Component;
//
//import java.math.RoundingMode;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//class CreditApplicationsAggregator {
//
//    @Bean
//    public KStream<String, ApplicationProcessedEvent> applicationProcessedEventStream(StreamsBuilder streamsBuilder) {
//        Serde<String> stringSerde = Serdes.String();
//        JsonSerde<ApplicationProcessedEvent> applicationProcessedEventSerde = new JsonSerde<>(ApplicationProcessedEvent.class);
//
//        KStream<String, ApplicationProcessedEvent> applicationStream =
//            streamsBuilder.stream("application-processed-events", Consumed.with(stringSerde, applicationProcessedEventSerde));
//
//        JsonSerde<AvgAggregator> avgAggregatorJsonSerde = new JsonSerde<>(AvgAggregator.class);
//        JsonSerde<CreditLimitSummary> creditLimitSummarySerde = new JsonSerde<>(CreditLimitSummary.class);
//
//        KTable<String, AvgAggregator> averageCreditLimitByDocumentType = applicationStream
//            .peek((key, value) -> log.info("Received application: {}, State: {}", key, value.applicationState()))
//            .filter((key, value) -> value.applicationState() == ApplicationState.APPROVED)
//            .map((key, value) -> {
//                log.info("Mapping: {}, Credit limit: {}", key, value.creditLimit());
//                return new KeyValue<>(value.type().toString(), value);
//            })
//            .groupByKey(Grouped.with(stringSerde, applicationProcessedEventSerde))
//            .aggregate(AvgAggregator::new, (key, value, aggregate) -> {
//                return aggregate.add(value.creditLimit());
//            }, Materialized.with(Serdes.String(), avgAggregatorJsonSerde));
//
//        averageCreditLimitByDocumentType.toStream()
//            .map(this::mapToSummary)
//            .peek((key, value) -> log.info("Publishing summary for: {}, Average: {}", key, value.getAverageCreditLimit()))
//            .to("averageCreditLimitByDocumentType", Produced.with(stringSerde, creditLimitSummarySerde));
//
//        Topology topology = streamsBuilder.build();
//        log.info("Topology: {}", topology.describe());
//
//        return applicationStream;
//    }
//
//
//    private KeyValue<String, CreditLimitSummary> mapToSummary(String key, AvgAggregator avgAggregator) {
//        log.info("Mapping to summary: {}, Average: {}", key, avgAggregator.average());
//        return new KeyValue<>(
//            key,
//            new CreditLimitSummary(key, avgAggregator.average().setScale(2, RoundingMode.HALF_UP), 0L, 0L)
//        );
//    }
//}
