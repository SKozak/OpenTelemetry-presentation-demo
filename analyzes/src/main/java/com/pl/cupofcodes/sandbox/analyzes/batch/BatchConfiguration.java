package com.pl.cupofcodes.sandbox.analyzes.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
class BatchConfiguration {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private final ObjectMapper objectMapper;

    @Bean
    public ConsumerFactory<String, BatchResult> batchConsumerFactory() {

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "batch-result-consumer-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        final JsonDeserializer<BatchResult> batchResultJsonDeserializer = new JsonDeserializer<>(BatchResult.class);
        batchResultJsonDeserializer.trustedPackages("*");
        batchResultJsonDeserializer.ignoreTypeHeaders();

        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                batchResultJsonDeserializer);
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter(objectMapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BatchResult> batchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BatchResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(batchConsumerFactory());
        return factory;
    }
}
