package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.kozak.telemetry.creditwise.creditcards.fraud.FraudFacade;
import pl.com.kozak.telemetry.creditwise.creditcards.identity.IdentityRegistryFacade;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.Outbox;

@Configuration
@RequiredArgsConstructor
class ApplicationsConfiguration {
    private final IdentityRegistryFacade identityRegistryFacade;
    private final JpaApplicationRepository jpaApplicationRepository;
    private final FraudFacade fraudFacade;
    private final Outbox manualOutbox;
    private final Outbox debeziumOutbox;

    @Bean
    ApplicationRepository applicationRepository() {
        return new JpaBasedApplicationRepository(jpaApplicationRepository);
    }

    @Bean
    ApplicationFacade applicationFacade(ApplicationRepository applicationRepository) {
        return new ApplicationFacade(
                applicationRepository,
                identityRegistryFacade,
                Mappers.getMapper(CreditCardApplicationMapper.class),
                fraudFacade,
                manualOutbox,
                "application-processed-events"
        );
    }

    @Bean
    ApplicationFacade applicationFacadeWithDebezium(ApplicationRepository applicationRepository) {
        return new ApplicationFacade(
                applicationRepository,
                identityRegistryFacade,
                Mappers.getMapper(CreditCardApplicationMapper.class),
                fraudFacade,
                debeziumOutbox,
                "application-processed-events"
        );
    }
}
