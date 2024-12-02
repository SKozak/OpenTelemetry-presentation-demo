package pl.com.kozak.telemetry.creditwise.fraud.migration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pl.com.kozak.telemetry.creditwise.fraud.check.PotentialRiskyAddress;
import pl.com.kozak.telemetry.creditwise.fraud.check.ReportedFraud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
class DatabaseInitializer {
    private final ReactiveMongoOperations reactiveMongoTemplate;

    @EventListener(ApplicationStartedEvent.class)
    public void initializeDatabase() {
        log.info("Initializing database with initial data...");
        Flux.fromIterable(DataGenerator.generateInitialAddresses())
            .flatMap(this::insertIfNotExist)
            .doOnError(throwable -> log.error("Error while initializing database: {}", throwable.getMessage()))
            .doOnComplete(() -> log.info("Database initialized"))
            .subscribe();

        Flux.fromIterable(DataGenerator.generateInitialFrauds())
            .flatMap(this::insertIfNotExist)
            .doOnError(throwable -> log.error("Error while initializing database: {}", throwable.getMessage()))
            .doOnComplete(() -> log.info("Database initialized"))
            .subscribe();

    }

    private Mono<PotentialRiskyAddress> insertIfNotExist(PotentialRiskyAddress address) {
        return reactiveMongoTemplate.findOne(
                Query.query(Criteria.where("street").is(address.getStreet())
                    .and("city").is(address.getCity())
                    .and("zip").is(address.getZip())),
                PotentialRiskyAddress.class)
            .switchIfEmpty(reactiveMongoTemplate.save(address));
    }

    private Mono<ReportedFraud> insertIfNotExist(ReportedFraud reportedFraud) {
        return reactiveMongoTemplate.findOne(
                Query.query(Criteria.where("name").is(reportedFraud.getName())
                    .and("email").is(reportedFraud.getEmail())
                    .and("dateOfBirth").is(reportedFraud.getDateOfBirth())),
                ReportedFraud.class)
            .switchIfEmpty(reactiveMongoTemplate.save(reportedFraud));
    }
}

@Configuration
@RequiredArgsConstructor
class DatabaseInitialConfiguration {
    private final ReactiveMongoOperations reactiveMongoTemplate;

    @Bean
    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer(reactiveMongoTemplate);
    }
}
