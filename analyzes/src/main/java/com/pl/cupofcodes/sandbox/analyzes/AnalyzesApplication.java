package com.pl.cupofcodes.sandbox.analyzes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@EnableKafkaStreams
@SpringBootApplication
public class AnalyzesApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyzesApplication.class, args);
    }

}
