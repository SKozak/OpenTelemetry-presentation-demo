package pl.com.kozak.telemetry.creditwise.fraud.check;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
class FraudCheckRepository {

    private final ReactiveMongoOperations mongoOperations;

    Mono<Boolean> checkIfPreviouslyReported(FraudCheck fraudCheck) {
        final Query query = Query.query(Criteria.where("name").is(fraudCheck.name())
            .and("email").is(fraudCheck.email())
            .and("dateOfBirth").is(fraudCheck.dateOfBirth())
            .and("lastName").is(fraudCheck.lastName())
            .and("street").is(fraudCheck.street())
            .and("city").is(fraudCheck.city())
            .and("zip").is(fraudCheck.zip()));

        return mongoOperations.exists(query, ReportedFraud.class);
    }

    Mono<Boolean> checkIfAddressIsInHighRisk(PotentialRiskyAddress riskyAddress) {
        Query query = Query.query(
            Criteria.where("street").is(riskyAddress.getStreet())
                .and("city").is(riskyAddress.getCity())
                .and("zip").is(riskyAddress.getZip())
        );
        return mongoOperations.exists(query, PotentialRiskyAddress.class);

    }
}
